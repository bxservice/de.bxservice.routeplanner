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

/** Generated Interface for BAY_Delivery
 *  @author iDempiere (generated) 
 *  @version Release 4.1
 */
@SuppressWarnings("all")
public interface I_BAY_Delivery 
{

    /** TableName=BAY_Delivery */
    public static final String Table_Name = "BAY_Delivery";

    /** AD_Table_ID=1000029 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name A_Asset_ID */
    public static final String COLUMNNAME_A_Asset_ID = "A_Asset_ID";

	/** Set Asset.
	  * Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID);

	/** Get Asset.
	  * Asset used internally or by customers
	  */
	public int getA_Asset_ID();

	public org.compiere.model.I_A_Asset getA_Asset() throws RuntimeException;

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

    /** Column name BAY_CoDriver_ID */
    public static final String COLUMNNAME_BAY_CoDriver_ID = "BAY_CoDriver_ID";

	/** Set Co-Driver	  */
	public void setBAY_CoDriver_ID (int BAY_CoDriver_ID);

	/** Get Co-Driver	  */
	public int getBAY_CoDriver_ID();

	public org.compiere.model.I_S_Resource getBAY_CoDriver() throws RuntimeException;

    /** Column name BAY_Delivery_ID */
    public static final String COLUMNNAME_BAY_Delivery_ID = "BAY_Delivery_ID";

	/** Set Delivery	  */
	public void setBAY_Delivery_ID (int BAY_Delivery_ID);

	/** Get Delivery	  */
	public int getBAY_Delivery_ID();

    /** Column name BAY_Delivery_UU */
    public static final String COLUMNNAME_BAY_Delivery_UU = "BAY_Delivery_UU";

	/** Set BAY_Delivery_UU	  */
	public void setBAY_Delivery_UU (String BAY_Delivery_UU);

	/** Get BAY_Delivery_UU	  */
	public String getBAY_Delivery_UU();

    /** Column name BAY_Driver_ID */
    public static final String COLUMNNAME_BAY_Driver_ID = "BAY_Driver_ID";

	/** Set Driver	  */
	public void setBAY_Driver_ID (int BAY_Driver_ID);

	/** Get Driver	  */
	public int getBAY_Driver_ID();

	public org.compiere.model.I_S_Resource getBAY_Driver() throws RuntimeException;

    /** Column name BAY_RouteDate */
    public static final String COLUMNNAME_BAY_RouteDate = "BAY_RouteDate";

	/** Set Date	  */
	public void setBAY_RouteDate (Timestamp BAY_RouteDate);

	/** Get Date	  */
	public Timestamp getBAY_RouteDate();

    /** Column name BAY_Route_ID */
    public static final String COLUMNNAME_BAY_Route_ID = "BAY_Route_ID";

	/** Set Route	  */
	public void setBAY_Route_ID (int BAY_Route_ID);

	/** Get Route	  */
	public int getBAY_Route_ID();

	public I_BAY_Route getBAY_Route() throws RuntimeException;

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
}
