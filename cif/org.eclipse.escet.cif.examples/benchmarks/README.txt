##############################################################################
# Copyright (c) 2022 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available
# under the terms of the MIT License which is available at
# https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
##############################################################################

The CIF benchmarking scripts can be used to benchmark the CIF data-based synthesis tool. You may for instance compare
the performance of data-based synthesis for different variable ordering algorithms, given different random initial
variable orderings, for various seeds.

To perform a series of benchmarks for different benchmarking models, follow these steps:

* Configure the configurations you want to benchmark in 'benchmark__settings.tooldef'. The scripts has extensive
  comments that explain how to configure the benchmarking configurations.
* In the 'Applications' view, disable the 'Auto Terminate' option, to allow running multiple ToolDef scripts in
  parallel.
* Execute each of the ToolDef scripts for benchmarking of individual benchmarking models, in parallel. These are the
  'benchmark_*.tooldef' scripts with only a single underscore ('_') rather than two underscores ('__'). For each
  such file, either right click it and choose 'Execute ToolDef', or left click it and press the 'F10' key.
* Wait for all the executions to have finished. You can observe the progress in the 'Applications' view. You can also
  look at the progress on the 'Console' view, where you can switch to different executions by using the small downward
  arrow next to the 'Display Selected Console' button at the top right of the 'Console' view.
* Once all executions have finished, execute the 'benchmark__generate_overview.tooldef' script, similar to how you
  executed the other ToolDef scripts.
* Open '_generated/_overview.html' to see the generated overview of benchmarking results.
* Rename the '_generated' folder to preserve it before executing any other benchmarks.

NOTE: synthesis takes longer when benchmarking due to the collection of platform-independent performance metrics.
