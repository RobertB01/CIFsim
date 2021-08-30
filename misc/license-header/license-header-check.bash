#!/usr/bin/env bash

################################################################################
# Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################

# Go to Git repository root.
SCRIPT=`readlink -f $0`
SCRIPTPATH=`dirname $SCRIPT`
cd $SCRIPTPATH/../..

# Print what we're doing.
echo "Checking license headers..."

# Prepare excludes.
EXCLUDE_ARGS=
# Exclude Git ignores.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude-dir=target"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=.polyglot.*"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=.META-INF_MANIFEST.MF"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=feature.xml.takari_issue_192"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=pom.tycho"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=.polyglot.feature.xml"
# Exclude generated files.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.target"
# Exclude Git repository metadata.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude-dir=.git"
# Exclude binary files.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.bmp"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.class"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.gif"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.ico"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.jar"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.jpg"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.pdf"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.png"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.slx"
# Exclude text files that don't support comments.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=MANIFEST.MF"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.bib"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.json"
# Exclude text files where Eclipse removes comments when editing them.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.aird"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.ecore"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.genmodel"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.launch"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.setup"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=.checkstyle"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=.classpath"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=.project"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=org.eclipse.core.resources.prefs"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=org.eclipse.jdt.core.prefs"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=org.eclipse.jdt.launching.prefs"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=org.eclipse.jdt.ui.prefs"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=org.eclipse.m2e.core.prefs"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=org.eclipse.pde.core.prefs"
# Exclude expected test output files.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.err"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.out"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.out.cif"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.ctrlsys.cif"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.elim.cif"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.expected.cif"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.merged.cif"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.statespace.cif"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*.trajdata.expected"
# Exclude feature.properties, to be replaced by license feature content.
# Exception is license feature itself, which has been checked manually to have a header.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=feature.properties"
# Exclude legal files known to not include it.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=about.html"
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=license.html"
# Exclude files indicating third party licenses.
EXCLUDE_ARGS="$EXCLUDE_ARGS --exclude=*_license.txt"

# Prepare post excludes (can't exclude specifically enough with grep).
POST_EXCLUDE_PATTERN="^$"
# Exclude files that explicitly should not have a license header.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./CONTRIBUTING.asciidoc:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./DEPENDENCIES.txt:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./NOTICE.asciidoc:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./misc/license-header/license-header-list.txt:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.product.feature/extra_files/NOTICE.asciidoc:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./releng/org.eclipse.escet.license.mit/NOTICE.asciidoc:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./.*/empty.txt:0$"
# Exclude expected test output files.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2mcrl2/.*.dbg.txt:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2mcrl2/.*.mcrl2:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*.plcopen.xml:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_iec/.*.plccfg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_iec/.*.plcfunc:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_iec/.*.plcprog:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_iec/.*.plctype:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_twincat/.*.plcproj:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_twincat/.*.sln:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_twincat/.*.tsproj:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_twincat/.*.TcDUT:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_twincat/.*.TcGVL:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_twincat/.*.TcPOU:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_twincat/.*.TcTTO:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2plc/.*_twincat/.*.plcproj:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2supremica/.*.wmod:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2uppaal/.*.xml:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/cif2yed/.*.graphml:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_compile.sh:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_engine.c:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_engine.h:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_library.c:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_library.h:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_readme.txt:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_test_code.c:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_java/.*.java:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_simulink/.*.c:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/codegen/.*_simulink/.*_report.txt:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/event_disabler/.*.disabled.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/explorer/.*.aut.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/explorer/.*.report.txt:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/event_based/.*.expected.txt:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/simulator/print/print_file.cif.txt:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./setext/org.eclipse.escet.setext.tests/test_models/.*.bnf.exp:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./setext/org.eclipse.escet.setext.tests/test_models/.*.dbg.exp:0$"
# Exclude test input files that don't support a header.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./chi/org.eclipse.escet.chi.tests/tests/examples/rlist.dat:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./chi/org.eclipse.escet.chi.tests/tests/spec/global/read_eol_eof.txt:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/tchecker/svg_file_io_error_empty.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.tests/tests/tchecker/svg_file_io_error_text_file.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./tooldef/org.eclipse.escet.tooldef.tests/tests/interpreter/subdir/.*.txt:0$"
# Exclude code generator template files.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.codegen/src/org/eclipse/escet/cif/codegen/.*/templates/.*:0$"
# Exclude no file extension .ecore file. Treat as other .ecore files.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./common/org.eclipse.escet.common.emf.tests/test_models/no_file_ext:0$"
# Exclude files with more than one known hits.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./chi/org.eclipse.escet.chi.documentation/asciidoc/index.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./chi/org.eclipse.escet.chi.documentation/asciidoc/legal.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./chi/org.eclipse.escet.chi.metamodel/docs/chi_ecore_doc.tex:3$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./chi/org.eclipse.escet.chi.metamodel/model/autofix.py:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/asciidoc/index.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/asciidoc/legal.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.metamodel/docs/cif_ecore_doc.tex:3$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.metamodel/model/autofix.py:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./common/org.eclipse.escet.common.app.framework/src/org/eclipse/escet/common/app/framework/options/HelpOption.java:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./common/org.eclipse.escet.common.emf.ecore.codegen/src/org/eclipse/escet/common/emf/ecore/codegen/java/EmfConstructorsGenerator.java:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./common/org.eclipse.escet.common.emf.ecore.codegen/src/org/eclipse/escet/common/emf/ecore/codegen/java/ModelWalkerGenerator.java:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./common/org.eclipse.escet.common.emf.ecore.codegen/src/org/eclipse/escet/common/emf/ecore/codegen/latex/EmfLatexDocSkeletonDetailGenerator.java:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./common/org.eclipse.escet.common.emf.ecore.codegen/src/org/eclipse/escet/common/emf/ecore/codegen/latex/EmfLatexDocSkeletonGenerator.java:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./common/org.eclipse.escet.common.position.metamodel/docs/position_ecore_doc.tex:3$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./common/org.eclipse.escet.common.position.metamodel/model/autofix.py:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./misc/license-header/license-header-check.bash:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/asciidoc/index.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/asciidoc/legal.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.product.branding/about.properties:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.product.branding/plugin.properties:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./setext/org.eclipse.escet.setext.documentation/asciidoc/index.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./setext/org.eclipse.escet.setext.documentation/asciidoc/legal.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./tooldef/org.eclipse.escet.tooldef.documentation/asciidoc/index.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./tooldef/org.eclipse.escet.tooldef.documentation/asciidoc/legal.asciidoc:2$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./tooldef/org.eclipse.escet.tooldef.metamodel/model/autofix.py:2$"
# Exclude files that have an associated license file.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.cif2plc/src/org/eclipse/escet/cif/cif2plc/writers/tc6_xml_v201.xsd:0$"
# Exclude files literally included in documentation.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/lang-ref/syntax/cif.bnf:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/buffers_products/buffers_products.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/buffers_products/buffers_products.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/lamps/lamps2.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/lamps/lamps3.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/lamps/lamps3.simple.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/rate/rate.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/sun_moon/sun_moon.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/tank/tank.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/walk_floor/walk_floor.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/walk_room/walk_room.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/workstation/workstation.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/lamp/lamp.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/lamp/lamp.tooldef:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgcopy/svgcopy.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgcopy/svgcopy.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgcopy/svgcopy.out.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgcopy/svgcopy.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgcopy/overlap.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgcopy/overlap_out1.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgcopy/overlap_out2.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgcopy/overlap_out3.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgmove/svgmove.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgmove/svgmove.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/svgviz/svgmove/svgmove.out.svg:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/tank_raw.trajdata:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/output/tank.trajdata:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/machine.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/machine.tooldef:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/solver/integr_linear.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/solver/integr_linear.png.dat:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/solver/integr_nonlinear.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/solver/integr_discont.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/solver/root_simple.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/solver/root_problem.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/solver/root_problem2.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/cifsim/solver/root_comma_fail.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/mergecif/merge_sup.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/mergecif/merge_timed.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./cif/org.eclipse.escet.cif.documentation/images/tools/mergecif/merged.cif:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./common/org.eclipse.escet.common.raildiagrams/src/org/eclipse/escet/common/raildiagrams/config/default.properties:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/abcd.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/abcdefgh.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/alternating.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/choice.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/choice2.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/emptyalternating.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/optional.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/refpath.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/sequence.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/sequence_loops.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/terminals.props:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./products/org.eclipse.escet.documentation/images/rail/terminals.rr:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./tooldef/org.eclipse.escet.tooldef.documentation/images/lang-ref/syntax/tooldef.bnf:0$"
# Exclude third party dependencies.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./thirdparty/.*\.properties:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./thirdparty/.*\.xml:0$"
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./thirdparty/.*/lgpl-.*.html:0$"
# Exclude files that are only present on the Jenkins build server.
POST_EXCLUDE_PATTERN="$POST_EXCLUDE_PATTERN|^./.Xauthority-.*:0$"

# Get license header count per file, using copyright statement from license header.
# Exclude ones with exactly one match, as that is the expected/desired situation.
grep -r -c $EXCLUDE_ARGS -E "Copyright \(c\) ([0-9]{4}, )?2021 Contributors to the Eclipse Foundation" . | \
    grep -v "^.*:1$" | grep -v -E $POST_EXCLUDE_PATTERN > misc/license-header/license-header-list.txt

# Print/check results.
cat misc/license-header/license-header-list.txt
VIOLATION_COUNT="$(cat misc/license-header/license-header-list.txt | wc -l)"
echo "$VIOLATION_COUNT issue(s) found..."
exit $VIOLATION_COUNT
