/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "OpenEJB" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of The OpenEJB Group.  For written permission,
 *    please contact openejb-group@openejb.sf.net.
 *
 * 4. Products derived from this Software may not be called "OpenEJB"
 *    nor may "OpenEJB" appear in their names without prior written
 *    permission of The OpenEJB Group. OpenEJB is a registered
 *    trademark of The OpenEJB Group.
 *
 * 5. Due credit should be given to the OpenEJB Project
 *    (http://openejb.sf.net/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE OPENEJB GROUP AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * THE OPENEJB GROUP OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) The OpenEJB Group. All Rights Reserved.
 *
 * $Id$
 */
package org.openejb.server.ejbd;

import org.apache.geronimo.kernel.Kernel;
import org.openejb.ContainerIndex;
import org.openejb.EJBContainer;
import org.openejb.client.*;
import org.openejb.proxy.ProxyInfo;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import javax.naming.NamingException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 */
class JndiRequestHandler implements ResponseCodes, RequestMethods {
    private final ContainerIndex containerIndex;

    JndiRequestHandler(ContainerIndex containerIndex) throws NamingException {
        this.containerIndex = containerIndex;
    }

    public void processRequest(ObjectInputStream in, ObjectOutputStream out) throws Exception {
        JNDIRequest req = new JNDIRequest();
        JNDIResponse res = new JNDIResponse();
        req.readExternal(in);

        // We are assuming that the request method is JNDI_LOOKUP
        // TODO: Implement the JNDI_LIST and JNDI_LIST_BINDINGS methods

        switch (req.getRequestMethod()) {
            case JNDI_LOOKUP:
                doLookup(req, res);
                break;
            case JNDI_LIST:
                doList(req, res);
                break;
            case JNDI_LIST_BINDINGS:
                doListBindings(req, res);
                break;
            default: throw new UnsupportedOperationException("Request method not supported: "+req.getRequestMethod());
        }

        res.writeExternal(out);
    }

    private void doListBindings(JNDIRequest req, JNDIResponse res) {
        //TODO Implement listbindings
        throw new UnsupportedOperationException("List bindings operation not implemented");
    }

    private void doList(JNDIRequest req, JNDIResponse res) {
        //TODO Implement list
        throw new UnsupportedOperationException("List operation not implemented");
    }

    private void doLookup(JNDIRequest req, JNDIResponse res) throws Exception {
        String name = req.getRequestString();
        if (name.startsWith("/")) {
            name = name.substring(1);
        }

        if (req.getClientModuleID() != null) {
            try {
                ObjectName objectName = new ObjectName(req.getClientModuleID());
                Kernel.getSingleKernel().getAttribute(objectName, "componentContext");
            } catch (MalformedObjectNameException e) {
                throw (Exception)new NamingException("Invalid client module id in request: "+req.getClientModuleID()).initCause(e);
            } catch (Exception e) {
                throw (Exception)new NamingException("Unable to retrieve context for module: "+req.getClientModuleID()).initCause(e);
            }
        } else {
            int index = containerIndex.getContainerIndexByJndiName(name);
            if (index > 0) {
                EJBContainer deployment = containerIndex.getContainer(index);
                ProxyInfo info = deployment.getProxyInfo();

                res.setResponseCode(JNDI_EJBHOME);
                EJBMetaDataImpl metaData = new EJBMetaDataImpl(info.getHomeInterface(),
                        info.getRemoteInterface(),
                        info.getPrimaryKeyClass(),
                        info.getComponentType(),
                        info.getContainerID(),
                        index);
                res.setResult(metaData);
            } else {
                res.setResponseCode(JNDI_NOT_FOUND);
            }

        }
    }
}