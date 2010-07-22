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
package org.eclipse.e4.demo.mailapp.mailservice.domain;

import java.util.Date;

public interface IMail {
	public String getFrom();
	public String getTo();
	public String getSubject();
	public String getBody();
	public void setFrom(String string);
	public void setSubject(String string);
	public void setBody(String string);
	public Date getDate();
	public void setDate(Date date);
	public void setTo(String to);
}
