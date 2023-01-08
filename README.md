# talendtools
Talend Studioで作成されたジョブ（.itemファイル）から使用されているコンポーネントを抽出し、Excelファイルに出力するツールです。
試験的な機能として、統計ファイルから実行されたコンポーネントと実行時間を追加することも可能です。

 ## Table of Contents
- [talendtools](#talendtools)
  - [Table of Contents](#table-of-contents)
  - [Environment](#environment)
  - [Config](#config)
    - [Parameters](#parameters)
    - [properties file](#properties-file)
  - [build](#build)
  - [run](#run)
  - [OTUPUT](#otuput)
  - [STATS FILE（統計情報）について](#stats-file統計情報について)
## Environment
- JVM: jdk11
- gradle: 7.5

## Config
### Parameters
```
 -h,--help                 help
 -o,--outputDir <arg>      output_directory
 -p,--projectName <arg>    talend_project_name
 -s,--statFilePath <arg>   stat_file_path
 -show                     show conponent structure
 -w,--workspaceDir <arg>   talend_workspace_directory
```
### properties file
TlBuilder.properties
```
DEFAULT_WORKSPACE_DIR=/Applications/TalendStudio-8.0.1/studio/workspace
DEFAULT_PROJECT_NAME=SAMPLE
DEFAULT_OUTPUT_DIR=.
```
## build
```
tjtool$ ./gradlew shadowJar
```

## run
```
$ java -jar app-all.jar -h                     
```

## OTUPUT
console
```
project: SAMPLE
	job: CreateDatasheet_0.1
	jobuFileName: /Applications/TalendStudio-8.0.1/studio/workspace/SAMPLE/process/tools/CreateDatasheet_0.1.item
		id: tDBConnection_1  type: tMSSqlConnection
		id: tDBInput_1  type: tMSSqlInput
```

excel
![excel_output](figure/excel_out.png "excel output")

## STATS FILE（統計情報）について
Talend Studioにて各コンポーネントの詳細設定の統計情報出力をチェックすることでコンポーネントごとの実行の開始／終了を統計情報ファイルに出力することが可能です。ただし、各コンポーネントごとに手動で行う必要があります。
この統計情報ファイルを読み込ませることにより、実行されたかどうか及び実行時間がわかります。
このプログラムでは実行された回数と実行時間の累積をコンポーネントごとに集計します。
