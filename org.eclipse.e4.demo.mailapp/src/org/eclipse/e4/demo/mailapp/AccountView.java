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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.demo.mailapp.mailservice.IMailSession;
import org.eclipse.e4.demo.mailapp.mailservice.IMailSession.ISessionListener;
import org.eclipse.e4.demo.mailapp.mailservice.IMailSessionFactory;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IAccount;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolder;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolderContainer;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class AccountView {
	private IMailSessionFactory mailSessionFactory;
	
	private IMailSession mailSession;
	
	private TreeViewer viewer;
	
	private String username = "john";
	
	private String password = "doe";
	
	private String host = "tomsondev.bestsolution.com";
	
	private boolean modified = false;
	
	@Inject
	@Optional
	private ESelectionService selectionService;
	
	private ISessionListener listener;
	
	@Inject
	@Optional
	private IEventBroker eventBroker;

	@Inject
	@Optional
	private MApplication application;
	
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
		
		listener = new ISessionListener() {
			
			public void mailAdded(IFolder folder, IMail mail) {
				if( eventBroker != null ) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put(EventConstants.NEW_MAIL_TAG_FOLDER, folder);
					map.put(EventConstants.NEW_MAIL_TAG_MAIL, mail);
					eventBroker.post(EventConstants.NEW_MAIL, map);
				}
			}
		};
		
		// Work around for 4.0 Bug of not cleaning up on Window-close
		viewer.getControl().addDisposeListener(new DisposeListener() {
			
			public void widgetDisposed(DisposeEvent e) {
				cleanUp();
			}
		});
	}
	
	@Inject
	public void setUsername(@Preference("username") String username) {
		this.username = username;
		this.modified = true;
	}
	
	@Inject
	public void setPassword(@Preference("password") String password) {
		this.password = password;
		this.modified = true;
	}
	
	@Inject
	public void setHost(@Preference("host") String host) {
		this.host = host;
		this.modified = true;
	}
	
	@PostConstruct
	public void init() {
		if( username != null && password != null && host != null ) {
			
			if( mailSession != null ) {
				mailSession.removeListener(listener);
			}
			
			mailSession = mailSessionFactory.openSession(host, username, password);
			if( mailSession != null ) {
				viewer.setInput(mailSession.getAccounts());
				mailSession.addListener(listener);
			} else {
				viewer.setInput(new WritableList());
			}
		}
		modified = false;
		if( application != null ) {
			application.getContext().set(IMailSession.class, mailSession);	
		}
	}
	
	@PreDestroy
	void cleanUp() {
		if( mailSession != null && listener != null ) {
			mailSession.removeListener(listener);	
		}
	}
	
	@Focus
	void onFocus(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		if( modified ) {
			if( MessageDialog.openQuestion(shell, "AccountInfos Modified", "The account informations have been modified would you like to reconnect with them?") ) {
				init();
				if( mailSession == null ) {
					MessageDialog.openWarning(shell, "Connection failed", "Opening a connecting to the mail server failed.");
				}
			}
		}
		if( application != null ) {
			application.getContext().set(IMailSession.class, mailSession);	
		}
	}
}