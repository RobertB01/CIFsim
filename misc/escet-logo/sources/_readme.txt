//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

These steps are known to work with Inkscape 1.1.2.

To create the ESCET logo with black text, follow these steps:

- Open 'eclipse-escet-logo.editable.svg' in Inkscape.
- Select the 'text-eclipse' object in the Inkscape XML Editor.
- Open the 'Path' menu and select 'Object to Path', to make sure it renders the same on all systems.
- Use the Inkscape XML Editor to assign ids 'text-eclipse1', 'text-eclipse2', ..., 'text-eclipse7' to the letter paths.
- Similar convert 'ESCET' and 'TM' to paths, and assign new ids to their letters.
- Save the file as a 'Plain SVG' file, named 'eclipse-escet-logo-black-text.svg'.
- Use 'File -> Export PNG Image...', make sure 'Page' is selected, and press 'Export'.

To create the ESCET logo with white text, follow these steps:

- Open 'eclipse-escet-logo.editable.svg' in Inkscape.
- Change the text color of the 'Eclipse', 'ESCET' and 'TM' texts to white.
- Follow the same steps as above, but save to 'eclipse-escet-logo-white-text.svg' instead.

