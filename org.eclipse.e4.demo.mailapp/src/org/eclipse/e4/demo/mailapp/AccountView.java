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
package org.eclipse.e4.demo.mailapp;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.demo.mailapp.mailservice.IMailSession;
import org.eclipse.e4.demo.mailapp.mailservice.IMailSessionFactory;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IAccount;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolder;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolderContainer;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

@SuppressWarnings("restriction")
public class AccountView {
	private IMailSessionFactory mailSessionFactory;
	
	private IMailSession mailSession;
	
	private TreeViewer viewer;
	
	private String username = "john";
	
	private String password = "doe";
	
	private String host = "tomsondev.bestsolution.com";
	
	@Inject
	@Optional
	private ESelectionService selectionService;
	
	@Inject
	public AccountView(Composite parent, IMailSessionFactory mailSessionFactory) {
		this.mailSessionFactory = mailSessionFactory;
		viewer = new TreeViewer(parent,SWT.FULL_SELECTION);
		viewer.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if( element instanceof IAccount ) {
					return ((IAccount) element).getName();
				} else if( element instanceof IFolder ) {
					return ((IFolder)element).getName();
				}
				return super.getText(element);
			}
		});
		
		IObservableFactory factory = new IObservableFactory() {
			private IListProperty prop = BeanProperties.list("folders");
			
			public IObservable createObservable(Object target) {
				if( target instanceof IObservableList ) {
					return (IObservable) target;
				} else if( target instanceof IFolderContainer ) {
					return prop.observe(target);
				}
				return null;
			}
		};
		
		TreeStructureAdvisor advisor = new TreeStructureAdvisor() {
		};
		
		viewer.setContentProvider(new ObservableListTreeContentProvider(factory, advisor));
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				if( selectionService != null ) {
					selectionService.setSelection(((IStructuredSelection)event.getSelection()).getFirstElement());
				}
			}
		});
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	@PostConstruct
	public void init() {
		if( username != null && password != null && host != null ) {
			mailSession = mailSessionFactory.openSession(host, username, password);
			viewer.setInput(mailSession.getAccounts());			
		}
	}
}
