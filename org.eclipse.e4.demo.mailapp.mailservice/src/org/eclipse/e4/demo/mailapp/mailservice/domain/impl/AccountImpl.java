/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.mailapp.mailservice.domain.impl;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.e4.demo.mailapp.mailservice.IMailSession;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IAccount;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolder;

public class AccountImpl extends BaseBean implements IAccount {
	private String name;
	private List<IFolder> folders = new ArrayList<IFolder>();
	private IMailSession session;
		
	public AccountImpl(IMailSession session) {
		this.session = session;
	}

	public IMailSession getSession() {
		return session;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange(new PropertyChangeEvent(this, "name", this.name,
				this.name = name));
	}

	public List<IFolder> getFolders() {
		return Collections.unmodifiableList(folders);
	}

	public void addFolder(IFolder folder) {
		folders.add(folder);
		firePropertyChange(new IndexedPropertyChangeEvent(this, "folders",
				null, folder, folders.size() - 1));
		((FolderImpl)folder).setContainer(this);
	}
}
