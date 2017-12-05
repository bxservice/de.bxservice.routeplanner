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
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Model for BAY_Route
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_BAY_Route extends PO implements I_BAY_Route, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171205L;

    /** Standard Constructor */
    public X_BAY_Route (Properties ctx, int BAY_Route_ID, String trxName)
    {
      super (ctx, BAY_Route_ID, trxName);
      /** if (BAY_Route_ID == 0)
        {
			setBAY_Route_ID (0);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_BAY_Route (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_BAY_Route[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Extraordinary Route.
		@param BAY_isExtraordinary 
		Defines if the route is extraordinary.
	  */
	public void setBAY_isExtraordinary (boolean BAY_isExtraordinary)
	{
		set_Value (COLUMNNAME_BAY_isExtraordinary, Boolean.valueOf(BAY_isExtraordinary));
	}

	/** Get Extraordinary Route.
		@return Defines if the route is extraordinary.
	  */
	public boolean isBAY_isExtraordinary () 
	{
		Object oo = get_Value(COLUMNNAME_BAY_isExtraordinary);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_BAY_Route getBAY_MasterRoute() throws RuntimeException
    {
		return (I_BAY_Route)MTable.get(getCtx(), I_BAY_Route.Table_Name)
			.getPO(getBAY_MasterRoute_ID(), get_TrxName());	}

	/** Set Parent Route.
		@param BAY_MasterRoute_ID 
		A parent route can identify that this route will we always set us the parent.
	  */
	public void setBAY_MasterRoute_ID (int BAY_MasterRoute_ID)
	{
		if (BAY_MasterRoute_ID < 1) 
			set_Value (COLUMNNAME_BAY_MasterRoute_ID, null);
		else 
			set_Value (COLUMNNAME_BAY_MasterRoute_ID, Integer.valueOf(BAY_MasterRoute_ID));
	}

	/** Get Parent Route.
		@return A parent route can identify that this route will we always set us the parent.
	  */
	public int getBAY_MasterRoute_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BAY_MasterRoute_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Route.
		@param BAY_Route_ID Route	  */
	public void setBAY_Route_ID (int BAY_Route_ID)
	{
		if (BAY_Route_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_BAY_Route_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_BAY_Route_ID, Integer.valueOf(BAY_Route_ID));
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

	/** Set BAY_Route_UU.
		@param BAY_Route_UU BAY_Route_UU	  */
	public void setBAY_Route_UU (String BAY_Route_UU)
	{
		set_Value (COLUMNNAME_BAY_Route_UU, BAY_Route_UU);
	}

	/** Get BAY_Route_UU.
		@return BAY_Route_UU	  */
	public String getBAY_Route_UU () 
	{
		return (String)get_Value(COLUMNNAME_BAY_Route_UU);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp () 
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}