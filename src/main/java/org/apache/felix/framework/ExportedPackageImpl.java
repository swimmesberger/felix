/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.framework;

import java.util.List;
import org.apache.felix.framework.resolver.Module;
import org.apache.felix.framework.wiring.BundleCapabilityImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.osgi.service.packageadmin.ExportedPackage;

class ExportedPackageImpl implements ExportedPackage
{
    private final Felix m_felix;
    private final BundleImpl m_exportingBundle;
    private final Module m_exportingModule;
    private final BundleCapabilityImpl m_export;
    private final String m_pkgName;
    private final Version m_version;

    public ExportedPackageImpl(
        Felix felix, BundleImpl exporter, Module module, BundleCapabilityImpl export)
    {
        m_felix = felix;
        m_exportingBundle = exporter;
        m_exportingModule = module;
        m_export = export;
        m_pkgName = (String) m_export.getAttributes().get(BundleCapabilityImpl.PACKAGE_ATTR);
        m_version = (!m_export.getAttributes().containsKey(BundleCapabilityImpl.VERSION_ATTR))
            ? Version.emptyVersion
            : (Version) m_export.getAttributes().get(BundleCapabilityImpl.VERSION_ATTR);
    }

    public Bundle getExportingBundle()
    {
        // If the package is stale, then return null per the spec.
        if (m_exportingBundle.isStale())
        {
            return null;
        }
        return m_exportingBundle;
    }

    public Bundle[] getImportingBundles()
    {
        // If the package is stale, then return null per the spec.
        if (m_exportingBundle.isStale())
        {
            return null;
        }
        List<Bundle> list = m_felix.getImportingBundles(this);
        return list.toArray(new Bundle[list.size()]);
    }

    public String getName()
    {
        return m_pkgName;
    }

    public String getSpecificationVersion()
    {
        return m_version.toString();
    }

    public Version getVersion()
    {
        return m_version;
    }

    public boolean isRemovalPending()
    {
        return m_exportingBundle.isRemovalPending();
    }

    public String toString()
    {
        return m_pkgName + "; version=" + m_version;
    }
}