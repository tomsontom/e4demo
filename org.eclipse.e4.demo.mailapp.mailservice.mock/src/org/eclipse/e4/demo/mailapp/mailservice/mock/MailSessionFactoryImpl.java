/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of:
 * 
 * a) EPL
 * The Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * b) EDL
 * The Eclipse Development License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.mailapp.mailservice.mock;

import org.eclipse.e4.demo.mailapp.mailservice.IMailSession;
import org.eclipse.e4.demo.mailapp.mailservice.IMailSessionFactory;

public class MailSessionFactoryImpl implements IMailSessionFactory {

	public IMailSession openSession(String host, String username,
			String password) {
		if( "john".equals(username) && "doe".equals(password) ) {
			return new MailSessionImpl();
		}
		
		return null;
	}

}
