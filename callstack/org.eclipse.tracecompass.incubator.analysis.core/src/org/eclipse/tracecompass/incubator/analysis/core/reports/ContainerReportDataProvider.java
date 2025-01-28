/*******************************************************************************
 * Copyright (c) 2025 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.tracecompass.incubator.analysis.core.reports;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.tmf.core.config.ITmfConfiguration;
import org.eclipse.tracecompass.tmf.core.config.TmfConfiguration;
import org.eclipse.tracecompass.tmf.core.dataprovider.IDataProviderDescriptor;
import org.eclipse.tracecompass.tmf.core.dataprovider.IDataProviderDescriptor.ProviderType;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfConfigurationException;
import org.eclipse.tracecompass.tmf.core.model.DataProviderCapabilities;
import org.eclipse.tracecompass.tmf.core.model.DataProviderDescriptor;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

/**
 * Handler for container-specific report configurations
 *
 * @author Kaveh Shahedi
 */
public class ContainerReportDataProvider implements IReportDataProvider {

    private static final String CONTAINER_CONFIG_NAME = "Report Container ({0})"; //$NON-NLS-1$
    private static final String CONTAINER_CONFIG_DESCRIPTION = "Container for sub-reports: {0}"; //$NON-NLS-1$

    @Override
    public ReportProviderType getReportType() {
        return ReportProviderType.CONTAINER;
    }

    /**
     * Create a descriptor for a container configuration
     *
     * @param trace
     *            Trace to create the descriptor for
     * @param configuration
     *            Base configuration
     * @return Data provider descriptor
     * @throws TmfConfigurationException
     *             If configuration is invalid
     */
    @Override
    @SuppressWarnings("null")
    public @NonNull IDataProviderDescriptor createDescriptor(@NonNull ITmfTrace trace, @NonNull ITmfConfiguration configuration) throws TmfConfigurationException {
        validateConfiguration(configuration);

        String description = configuration.getDescription();
        if (description.equals(TmfConfiguration.UNKNOWN)) {
            description = NLS.bind(CONTAINER_CONFIG_DESCRIPTION, configuration.getName());
        }

        return getDescriptorFromConfig(trace, new TmfConfiguration.Builder()
                .setId(configuration.getId())
                .setSourceTypeId(configuration.getSourceTypeId())
                .setName(configuration.getName())
                .setDescription(description)
                .setParameters(configuration.getParameters())
                .build());
    }

    /**
     * Get a descriptor from a configuration
     *
     * @param trace
     *            The trace to create the descriptor for
     * @param configuration
     *            The configuration
     * @return The descriptor
     */
    @Override
    @SuppressWarnings("null")
    public @NonNull IDataProviderDescriptor getDescriptorFromConfig(@NonNull ITmfTrace trace, @NonNull ITmfConfiguration configuration) {
        return new DataProviderDescriptor.Builder()
                .setId(configuration.getId())
                .setName(NLS.bind(CONTAINER_CONFIG_NAME, configuration.getName()))
                .setDescription(configuration.getDescription())
                .setProviderType(ProviderType.NONE)
                .setConfiguration(configuration)
                .setCapabilities(new DataProviderCapabilities.Builder()
                        .setCanDelete(true)
                        .setCanCreate(true) // Container can have children
                        .build())
                .build();
    }

    @Override
    public void validateConfiguration(@NonNull ITmfConfiguration configuration) throws TmfConfigurationException {

    }

    @Override
    public @NonNull IDataProviderDescriptor removeDescriptor(@NonNull ITmfTrace trace, @NonNull ITmfConfiguration configuration) throws TmfConfigurationException {
        return getDescriptorFromConfig(trace, configuration);
    }
}
