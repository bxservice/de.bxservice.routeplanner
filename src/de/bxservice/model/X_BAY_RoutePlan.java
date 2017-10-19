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

/** Generated Model for BAY_RoutePlan
 *  @author iDempiere (generated) 
 *  @version Release 4.1 - $Id$ */
public class X_BAY_RoutePlan extends PO implements I_BAY_RoutePlan, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171019L;

    /** Standard Constructor */
    public X_BAY_RoutePlan (Properties ctx, int BAY_RoutePlan_ID, String trxName)
    {
      super (ctx, BAY_RoutePlan_ID, trxName);
      /** if (BAY_RoutePlan_ID == 0)
        {
			setBAY_Route_ID (0);
			setBAY_RoutePlan_ID (0);
        } */
    }

    /** Load Constructor */
    public X_BAY_RoutePlan (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_BAY_RoutePlan[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Postal Text.
		@param BAY_PostalText Postal Text	  */
	public void setBAY_PostalText (String BAY_PostalText)
	{
		set_Value (COLUMNNAME_BAY_PostalText, BAY_PostalText);
	}

	/** Get Postal Text.
		@return Postal Text	  */
	public String getBAY_PostalText () 
	{
		return (String)get_Value(COLUMNNAME_BAY_PostalText);
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

	/** Set Route Plan.
		@param BAY_RoutePlan_ID Route Plan	  */
	public void setBAY_RoutePlan_ID (int BAY_RoutePlan_ID)
	{
		if (BAY_RoutePlan_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_BAY_RoutePlan_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_BAY_RoutePlan_ID, Integer.valueOf(BAY_RoutePlan_ID));
	}

	/** Get Route Plan.
		@return Route Plan	  */
	public int getBAY_RoutePlan_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BAY_RoutePlan_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set BAY_RoutePlan_UU.
		@param BAY_RoutePlan_UU BAY_RoutePlan_UU	  */
	public void setBAY_RoutePlan_UU (String BAY_RoutePlan_UU)
	{
		set_Value (COLUMNNAME_BAY_RoutePlan_UU, BAY_RoutePlan_UU);
	}

	/** Get BAY_RoutePlan_UU.
		@return BAY_RoutePlan_UU	  */
	public String getBAY_RoutePlan_UU () 
	{
		return (String)get_Value(COLUMNNAME_BAY_RoutePlan_UU);
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

	/** WeekDay AD_Reference_ID=167 */
	public static final int WEEKDAY_AD_Reference_ID=167;
	/** Sunday = 7 */
	public static final String WEEKDAY_Sunday = "7";
	/** Monday = 1 */
	public static final String WEEKDAY_Monday = "1";
	/** Tuesday = 2 */
	public static final String WEEKDAY_Tuesday = "2";
	/** Wednesday = 3 */
	public static final String WEEKDAY_Wednesday = "3";
	/** Thursday = 4 */
	public static final String WEEKDAY_Thursday = "4";
	/** Friday = 5 */
	public static final String WEEKDAY_Friday = "5";
	/** Saturday = 6 */
	public static final String WEEKDAY_Saturday = "6";
	/** Set Day of the Week.
		@param WeekDay 
		Day of the Week
	  */
	public void setWeekDay (String WeekDay)
	{

		set_Value (COLUMNNAME_WeekDay, WeekDay);
	}

	/** Get Day of the Week.
		@return Day of the Week
	  */
	public String getWeekDay () 
	{
		return (String)get_Value(COLUMNNAME_WeekDay);
	}
}