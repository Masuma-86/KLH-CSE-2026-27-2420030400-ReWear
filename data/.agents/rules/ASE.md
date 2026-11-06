# ASE (Automated Software Engineering) Profile Guidelines

This workspace is operating under the **ASE (Automated Software Engineering)** profile for ChangeLens.

## Core Rules for ASE Profile:
1. **Automated Blast Radius Verification**: Every code change must have its downstream dependency graph evaluated before merging.
2. **Backward-Compatibility Safeguards**: Any function signature modification must provide default parameters or backwards-compatible overloads.
3. **Automated Test Skeleton Generation**: All detected breaking contracts must have corresponding unit test skeletons generated and attached.
4. **Jira Issue Synchronization**: High and Critical risk assessments must automatically synchronize mitigation tasks to the connected Jira sprint board.
