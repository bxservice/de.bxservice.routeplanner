/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package de.bxservice.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for BAY_Delivery
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_BAY_Delivery extends PO implements I_BAY_Delivery, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171019L;

    /** Standard Constructor */
    public X_BAY_Delivery (Properties ctx, int BAY_Delivery_ID, String trxName)
    {
      super (ctx, BAY_Delivery_ID, trxName);
      /** if (BAY_Delivery_ID == 0)
        {
			setBAY_Delivery_ID (0);
			setBAY_Driver_ID (0);
        } */
    }

    /** Load Constructor */
    public X_BAY_Delivery (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_BAY_Delivery[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_A_Asset getA_Asset() throws RuntimeException
    {
		return (org.compiere.model.I_A_Asset)MTable.get(getCtx(), org.compiere.model.I_A_Asset.Table_Name)
			.getPO(getA_Asset_ID(), get_TrxName());	}

	/** Set Asset.
		@param A_Asset_ID 
		Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID < 1) 
			set_Value (COLUMNNAME_A_Asset_ID, null);
		else 
			set_Value (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_S_Resource getBAY_CoDriver() throws RuntimeException
    {
		return (org.compiere.model.I_S_Resource)MTable.get(getCtx(), org.compiere.model.I_S_Resource.Table_Name)
			.getPO(getBAY_CoDriver_ID(), get_TrxName());	}

	/** Set Co-Driver.
		@param BAY_CoDriver_ID Co-Driver	  */
	public void setBAY_CoDriver_ID (int BAY_CoDriver_ID)
	{
		if (BAY_CoDriver_ID < 1) 
			set_Value (COLUMNNAME_BAY_CoDriver_ID, null);
		else 
			set_Value (COLUMNNAME_BAY_CoDriver_ID, Integer.valueOf(BAY_CoDriver_ID));
	}

	/** Get Co-Driver.
		@return Co-Driver	  */
	public int getBAY_CoDriver_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BAY_CoDriver_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Delivery.
		@param BAY_Delivery_ID Delivery	  */
	public void setBAY_Delivery_ID (int BAY_Delivery_ID)
	{
		if (BAY_Delivery_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_BAY_Delivery_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_BAY_Delivery_ID, Integer.valueOf(BAY_Delivery_ID));
	}

	/** Get Delivery.
		@return Delivery	  */
	public int getBAY_Delivery_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BAY_Delivery_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set BAY_Delivery_UU.
		@param BAY_Delivery_UU BAY_Delivery_UU	  */
	public void setBAY_Delivery_UU (String BAY_Delivery_UU)
	{
		set_Value (COLUMNNAME_BAY_Delivery_UU, BAY_Delivery_UU);
	}

	/** Get BAY_Delivery_UU.
		@return BAY_Delivery_UU	  */
	public String getBAY_Delivery_UU () 
	{
		return (String)get_Value(COLUMNNAME_BAY_Delivery_UU);
	}

	public org.compiere.model.I_S_Resource getBAY_Driver() throws RuntimeException
    {
		return (org.compiere.model.I_S_Resource)MTable.get(getCtx(), org.compiere.model.I_S_Resource.Table_Name)
			.getPO(getBAY_Driver_ID(), get_TrxName());	}

	/** Set Driver.
		@param BAY_Driver_ID Driver	  */
	public void setBAY_Driver_ID (int BAY_Driver_ID)
	{
		if (BAY_Driver_ID < 1) 
			set_Value (COLUMNNAME_BAY_Driver_ID, null);
		else 
			set_Value (COLUMNNAME_BAY_Driver_ID, Integer.valueOf(BAY_Driver_ID));
	}

	/** Get Driver.
		@return Driver	  */
	public int getBAY_Driver_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BAY_Driver_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Date.
		@param BAY_RouteDate Date	  */
	public void setBAY_RouteDate (Timestamp BAY_RouteDate)
	{
		set_Value (COLUMNNAME_BAY_RouteDate, BAY_RouteDate);
	}

	/** Get Date.
		@return Date	  */
	public Timestamp getBAY_RouteDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_BAY_RouteDate);
	}

	public I_BAY_Route getBAY_Route() throws RuntimeException
    {
		return (I_BAY_Route)MTable.get(getCtx(), I_BAY_Route.Table_Name)
			.getPO(getBAY_Route_ID(), get_TrxName());	}

	/** Set Route.
		@param BAY_Route_ID Route	  */
	public void setBAY_Route_ID (int BAY_Route_ID)
	{
		if (BAY_Route_ID < 1) 
			set_Value (COLUMNNAME_BAY_Route_ID, null);
		else 
			set_Value (COLUMNNAME_BAY_Route_ID, Integer.valueOf(BAY_Route_ID));
	}

	/** Get Route.
		@return Route	  */
	public int getBAY_Route_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BAY_Route_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}