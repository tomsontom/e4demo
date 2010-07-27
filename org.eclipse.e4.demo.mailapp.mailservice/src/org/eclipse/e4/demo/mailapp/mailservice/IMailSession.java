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
 * is reproduced below, and available at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.mailapp.mailservice;

import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IAccount;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolder;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;

public interface IMailSession {
	public interface ISessionListener {
		public void mailAdded(IFolder folder, IMail mail);
	}
	
	public IObservableList getAccounts();
	public List<IMail> getMails(IFolder folder, int startIndex, int amount);
	public void addListener(ISessionListener listener);
	public void removeListener(ISessionListener listener);
	public IMail createMail();
	public void sendMail(IAccount account, IMail mail);
}
