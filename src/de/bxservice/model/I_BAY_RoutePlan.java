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
package de.bxservice.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for BAY_RoutePlan
 *  @author iDempiere (generated) 
 *  @version Release 4.1
 */
@SuppressWarnings("all")
public interface I_BAY_RoutePlan 
{

    /** TableName=BAY_RoutePlan */
    public static final String Table_Name = "BAY_RoutePlan";

    /** AD_Table_ID=1000028 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name BAY_PostalText */
    public static final String COLUMNNAME_BAY_PostalText = "BAY_PostalText";

	/** Set Postal Text	  */
	public void setBAY_PostalText (String BAY_PostalText);

	/** Get Postal Text	  */
	public String getBAY_PostalText();

    /** Column name BAY_Route_ID */
    public static final String COLUMNNAME_BAY_Route_ID = "BAY_Route_ID";

	/** Set Route	  */
	public void setBAY_Route_ID (int BAY_Route_ID);

	/** Get Route	  */
	public int getBAY_Route_ID();

	public I_BAY_Route getBAY_Route() throws RuntimeException;

    /** Column name BAY_RoutePlan_ID */
    public static final String COLUMNNAME_BAY_RoutePlan_ID = "BAY_RoutePlan_ID";

	/** Set Route Plan	  */
	public void setBAY_RoutePlan_ID (int BAY_RoutePlan_ID);

	/** Get Route Plan	  */
	public int getBAY_RoutePlan_ID();

    /** Column name BAY_RoutePlan_UU */
    public static final String COLUMNNAME_BAY_RoutePlan_UU = "BAY_RoutePlan_UU";

	/** Set BAY_RoutePlan_UU	  */
	public void setBAY_RoutePlan_UU (String BAY_RoutePlan_UU);

	/** Get BAY_RoutePlan_UU	  */
	public String getBAY_RoutePlan_UU();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name WeekDay */
    public static final String COLUMNNAME_WeekDay = "WeekDay";

	/** Set Day of the Week.
	  * Day of the Week
	  */
	public void setWeekDay (String WeekDay);

	/** Get Day of the Week.
	  * Day of the Week
	  */
	public String getWeekDay();
}
