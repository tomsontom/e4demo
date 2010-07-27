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
package org.eclipse.e4.demo.mailapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolder;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

@SuppressWarnings("restriction")
public class FolderView {
	private TableViewer viewer;
	
	@Inject
	@Optional
	private ESelectionService selectionService;
	
	@Inject
	@Optional
	private IEventBroker eventBroker;
	
	private EventHandler eventHandler;
	
	private IFolder folder;
	
	@Inject
	public FolderView(Composite parent, @Optional IStylingEngine styleEngine) {
		this.viewer = new TableViewer(parent);
		this.viewer.setContentProvider(new ArrayContentProvider());
		this.viewer.getTable().setHeaderVisible(true);
		this.viewer.getTable().setLinesVisible(true);
		if( styleEngine != null ) {
			styleEngine.setClassname(this.viewer.getControl(), "mailList");
		}
		
		
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("Subject");
		column.getColumn().setWidth(250);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IMail)element).getSubject();
			}
		});
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("From");
		column.getColumn().setWidth(200);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((IMail)element).getFrom();
			}
		});
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("Date");
		column.getColumn().setWidth(150);
		column.setLabelProvider(new ColumnLabelProvider() {
			private DateFormat format = SimpleDateFormat.getDateTimeInstance();
			@Override
			public String getText(Object element) {
				Date date = ((IMail)element).getDate();
				if( date != null ) {
					return format.format(date);	
				}
				return "-";
			}
		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				if( selectionService != null ) {
					selectionService.setSelection(((IStructuredSelection)event.getSelection()).getFirstElement());
				}
			}
		});
		
		// Work around for 4.0 Bug of not cleaning up on Window-close
		viewer.getControl().addDisposeListener(new DisposeListener() {
			
			public void widgetDisposed(DisposeEvent e) {
				unhookEvents();
			}
		});
	}
	
	@PostConstruct
	void hookEvents() {
		if( eventBroker != null ) {
			eventHandler = new EventHandler() {
				public void handleEvent(final Event event) {
					if( event.getProperty(EventConstants.NEW_MAIL_TAG_FOLDER) == folder ) {
						viewer.getControl().getDisplay().asyncExec(new Runnable() {
							
							public void run() {
								viewer.add(event.getProperty(EventConstants.NEW_MAIL_TAG_MAIL));
							}
						});
					}
				}
			};
			eventBroker.subscribe(EventConstants.NEW_MAIL, eventHandler);
		}
	}
	
	@PreDestroy
	void unhookEvents() {
		if( eventBroker != null && eventHandler != null ) {
			eventBroker.unsubscribe(eventHandler);
		}
	}
	
	@Inject
	public void setFolder(@Named(IServiceConstants.ACTIVE_SELECTION) @Optional IFolder folder) {
		if( folder != null ) {
			this.folder = folder;
			viewer.setInput(folder.getSession().getMails(folder, 0, folder.getMailCount()));
		}
	}
}