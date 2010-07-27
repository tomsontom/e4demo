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
package org.eclipse.e4.demo.mailapp.newmail;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.demo.mailapp.mailservice.IMailSession;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IAccount;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public class EditMailView {
	@Inject
	private MTrimmedWindow window;
	
	private ComboViewer viewer;
	
	private IObservableValue master = new WritableValue();
	
	private ObservablesManager manager = new ObservablesManager();
	
	@Inject
	public EditMailView(final Composite container) {
		manager.runAndCollect(new Runnable() {
			
			public void run() {
				createUI(container);	
			}
		});		
	}
	
	private void createUI(Composite container) {
		Composite parent = new Composite(container,SWT.NONE);
		parent.setLayout(new GridLayout(2, false));
		
		Label l = new Label(parent, SWT.NONE);
		l.setText("Account");
		
		viewer = new  ComboViewer(parent);
		viewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IAccount)element).getName();
			}
		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				window.getContext().set(IAccount.class, (IAccount)((IStructuredSelection)event.getSelection()).getFirstElement());
			}
		});
		viewer.setContentProvider(new ObservableListContentProvider());

		DataBindingContext dbc = new DataBindingContext();
		IWidgetValueProperty prop = WidgetProperties.text(SWT.Modify);
		
		l = new Label(parent, SWT.NONE);
		l.setText("To");
		
		Text t = new Text(parent, SWT.BORDER);
		t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dbc.bindValue(prop.observe(t), BeanProperties.value("to").observeDetail(master));
				
		l = new Label(parent, SWT.NONE);
		l.setText("Subject");
		
		t = new Text(parent, SWT.BORDER);
		t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dbc.bindValue(prop.observe(t), BeanProperties.value("subject").observeDetail(master));
		BeanProperties.value("subject").observeDetail(master).addValueChangeListener(new IValueChangeListener() {
			
			public void handleValueChange(ValueChangeEvent event) {
				String value = (String) event.diff.getNewValue();
				value = value == null ? "" : value;
				if( window != null ) {
					window.setLabel("New Mail: " + value);
				}
			}
		});
		
		t = new Text(parent, SWT.BORDER|SWT.MULTI|SWT.WRAP);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		t.setLayoutData(gd);
		dbc.bindValue(prop.observe(t), BeanProperties.value("body").observeDetail(master));
	}
	
	@PreDestroy
	void cleanUp() {
		manager.dispose();
	}
	
	@Inject
	void setMailSession(IMailSession session) {
		if( session != null ) {
			viewer.setInput(session.getAccounts());	
		} else {
			if( ! viewer.getControl().isDisposed() ) {
				viewer.setInput(new WritableList());				
			}
		}
	}
	
	@Inject
	@Optional
	void setMail(IMail mail) {
		master.setValue(mail);
	}
}
