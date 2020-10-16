/**
 * Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
 * 
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 * 
 * This program and the accompanying materials are made available under the terms
 * of the MIT License which is available at https://opensource.org/licenses/MIT
 * 
 * SPDX-License-Identifier: MIT
 * 
 * Disable Eclipse Java formatter for generated code file:
 * @formatter:off
 */
package org.eclipse.escet.common.position.metamodel.position.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Position</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl#getStartLine <em>Start Line</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl#getEndOffset <em>End Offset</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl#getStartColumn <em>Start Column</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl#getEndLine <em>End Line</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl#getEndColumn <em>End Column</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl#getStartOffset <em>Start Offset</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.impl.PositionImpl#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PositionImpl extends MinimalEObjectImpl.Container implements Position
{
    /**
     * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected String source = SOURCE_EDEFAULT;

    /**
     * The default value of the '{@link #getStartLine() <em>Start Line</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartLine()
     * @generated
     * @ordered
     */
    protected static final int START_LINE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getStartLine() <em>Start Line</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartLine()
     * @generated
     * @ordered
     */
    protected int startLine = START_LINE_EDEFAULT;

    /**
     * The default value of the '{@link #getEndOffset() <em>End Offset</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndOffset()
     * @generated
     * @ordered
     */
    protected static final int END_OFFSET_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getEndOffset() <em>End Offset</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndOffset()
     * @generated
     * @ordered
     */
    protected int endOffset = END_OFFSET_EDEFAULT;

    /**
     * The default value of the '{@link #getStartColumn() <em>Start Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartColumn()
     * @generated
     * @ordered
     */
    protected static final int START_COLUMN_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getStartColumn() <em>Start Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartColumn()
     * @generated
     * @ordered
     */
    protected int startColumn = START_COLUMN_EDEFAULT;

    /**
     * The default value of the '{@link #getEndLine() <em>End Line</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndLine()
     * @generated
     * @ordered
     */
    protected static final int END_LINE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getEndLine() <em>End Line</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndLine()
     * @generated
     * @ordered
     */
    protected int endLine = END_LINE_EDEFAULT;

    /**
     * The default value of the '{@link #getEndColumn() <em>End Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndColumn()
     * @generated
     * @ordered
     */
    protected static final int END_COLUMN_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getEndColumn() <em>End Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndColumn()
     * @generated
     * @ordered
     */
    protected int endColumn = END_COLUMN_EDEFAULT;

    /**
     * The default value of the '{@link #getStartOffset() <em>Start Offset</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartOffset()
     * @generated
     * @ordered
     */
    protected static final int START_OFFSET_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getStartOffset() <em>Start Offset</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartOffset()
     * @generated
     * @ordered
     */
    protected int startOffset = START_OFFSET_EDEFAULT;

    /**
     * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLocation()
     * @generated
     * @ordered
     */
    protected static final String LOCATION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLocation()
     * @generated
     * @ordered
     */
    protected String location = LOCATION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PositionImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass()
    {
        return PositionPackage.Literals.POSITION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getSource()
    {
        return source;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSource(String newSource)
    {
        String oldSource = source;
        source = newSource;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION__SOURCE, oldSource, source));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getStartLine()
    {
        return startLine;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setStartLine(int newStartLine)
    {
        int oldStartLine = startLine;
        startLine = newStartLine;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION__START_LINE, oldStartLine, startLine));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getEndOffset()
    {
        return endOffset;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setEndOffset(int newEndOffset)
    {
        int oldEndOffset = endOffset;
        endOffset = newEndOffset;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION__END_OFFSET, oldEndOffset, endOffset));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getStartColumn()
    {
        return startColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setStartColumn(int newStartColumn)
    {
        int oldStartColumn = startColumn;
        startColumn = newStartColumn;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION__START_COLUMN, oldStartColumn, startColumn));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getEndLine()
    {
        return endLine;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setEndLine(int newEndLine)
    {
        int oldEndLine = endLine;
        endLine = newEndLine;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION__END_LINE, oldEndLine, endLine));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getEndColumn()
    {
        return endColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setEndColumn(int newEndColumn)
    {
        int oldEndColumn = endColumn;
        endColumn = newEndColumn;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION__END_COLUMN, oldEndColumn, endColumn));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getStartOffset()
    {
        return startOffset;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setStartOffset(int newStartOffset)
    {
        int oldStartOffset = startOffset;
        startOffset = newStartOffset;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION__START_OFFSET, oldStartOffset, startOffset));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getLocation()
    {
        return location;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setLocation(String newLocation)
    {
        String oldLocation = location;
        location = newLocation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PositionPackage.POSITION__LOCATION, oldLocation, location));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType)
    {
        switch (featureID)
        {
            case PositionPackage.POSITION__SOURCE:
                return getSource();
            case PositionPackage.POSITION__START_LINE:
                return getStartLine();
            case PositionPackage.POSITION__END_OFFSET:
                return getEndOffset();
            case PositionPackage.POSITION__START_COLUMN:
                return getStartColumn();
            case PositionPackage.POSITION__END_LINE:
                return getEndLine();
            case PositionPackage.POSITION__END_COLUMN:
                return getEndColumn();
            case PositionPackage.POSITION__START_OFFSET:
                return getStartOffset();
            case PositionPackage.POSITION__LOCATION:
                return getLocation();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case PositionPackage.POSITION__SOURCE:
                setSource((String)newValue);
                return;
            case PositionPackage.POSITION__START_LINE:
                setStartLine((Integer)newValue);
                return;
            case PositionPackage.POSITION__END_OFFSET:
                setEndOffset((Integer)newValue);
                return;
            case PositionPackage.POSITION__START_COLUMN:
                setStartColumn((Integer)newValue);
                return;
            case PositionPackage.POSITION__END_LINE:
                setEndLine((Integer)newValue);
                return;
            case PositionPackage.POSITION__END_COLUMN:
                setEndColumn((Integer)newValue);
                return;
            case PositionPackage.POSITION__START_OFFSET:
                setStartOffset((Integer)newValue);
                return;
            case PositionPackage.POSITION__LOCATION:
                setLocation((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID)
    {
        switch (featureID)
        {
            case PositionPackage.POSITION__SOURCE:
                setSource(SOURCE_EDEFAULT);
                return;
            case PositionPackage.POSITION__START_LINE:
                setStartLine(START_LINE_EDEFAULT);
                return;
            case PositionPackage.POSITION__END_OFFSET:
                setEndOffset(END_OFFSET_EDEFAULT);
                return;
            case PositionPackage.POSITION__START_COLUMN:
                setStartColumn(START_COLUMN_EDEFAULT);
                return;
            case PositionPackage.POSITION__END_LINE:
                setEndLine(END_LINE_EDEFAULT);
                return;
            case PositionPackage.POSITION__END_COLUMN:
                setEndColumn(END_COLUMN_EDEFAULT);
                return;
            case PositionPackage.POSITION__START_OFFSET:
                setStartOffset(START_OFFSET_EDEFAULT);
                return;
            case PositionPackage.POSITION__LOCATION:
                setLocation(LOCATION_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID)
    {
        switch (featureID)
        {
            case PositionPackage.POSITION__SOURCE:
                return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
            case PositionPackage.POSITION__START_LINE:
                return startLine != START_LINE_EDEFAULT;
            case PositionPackage.POSITION__END_OFFSET:
                return endOffset != END_OFFSET_EDEFAULT;
            case PositionPackage.POSITION__START_COLUMN:
                return startColumn != START_COLUMN_EDEFAULT;
            case PositionPackage.POSITION__END_LINE:
                return endLine != END_LINE_EDEFAULT;
            case PositionPackage.POSITION__END_COLUMN:
                return endColumn != END_COLUMN_EDEFAULT;
            case PositionPackage.POSITION__START_OFFSET:
                return startOffset != START_OFFSET_EDEFAULT;
            case PositionPackage.POSITION__LOCATION:
                return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString()
    {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (source: ");
        result.append(source);
        result.append(", startLine: ");
        result.append(startLine);
        result.append(", endOffset: ");
        result.append(endOffset);
        result.append(", startColumn: ");
        result.append(startColumn);
        result.append(", endLine: ");
        result.append(endLine);
        result.append(", endColumn: ");
        result.append(endColumn);
        result.append(", startOffset: ");
        result.append(startOffset);
        result.append(", location: ");
        result.append(location);
        result.append(')');
        return result.toString();
    }

} //PositionImpl
