@echo on

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:: Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
::
:: See the NOTICE file(s) distributed with this work for additional
:: information regarding copyright ownership.
::
:: This program and the accompanying materials are made available under the terms
:: of the MIT License which is available at https://opensource.org/licenses/MIT
::
:: SPDX-License-Identifier: MIT
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

mvn -Dtycho.pomless.aggregator.names=releng,chi,cif,common,setext,thirdparty,tooldef,product %*
