# talendtools
 Talend Studioで作成したジョブの使用コンポーネントを一覧化したExcelファイルを作成します。
 また、Studioで作成したDB接続情報をテキスト化したり、テーブル作成DDLを出力する機能も追加しています。（Oracle, SqlServerのみ）
 （試験的オプション）試験的な機能として、統計ファイルから実行されたコンポーネントと実行時間を追加することも可能です。
## Environment
- JVM: jdk11
- gradle: 7.5

## Prepare
Download this project.
Source code developed by Talend Studio.
### build
```
talendtools$ ./gradlew shadowJar
```
### Move jar to your work
```
talendtools$ cp app/build/libs/app-all.jar ~/mywork 
```
### Test run show help
```
talendtools$ cd ~/mywork 
mywork$ java -jar app-all.jar -h
usage: [opts]
 -h,--help                 help
 -o,--outputDir <arg>      output_directory
 -out_components           output [project].xlsx and [project].txt
 -out_connections          output [db_connection.item].txt
 -out_ddl                  output create_[db_connection.item].sql
 -p,--projectName <arg>    talend_project_name
 -s,--statFilePath <arg>   stat_file_path
 -show                     show to concole
 -w,--workspaceDir <arg>   talend_workspace_directory
```
### Create properties file ( recommend )
```
mywork$ vi TlBuilder.properties
```
sample
```
DEFAULT_WORKSPACE_DIR=/Applications/TOSDI-8.0.1/studio/workspace (workspace of Talend Studio)
DEFAULT_PROJECT_NAME=SAMPLE (Project name)
DEFAULT_OUTPUT_DIR=~/tmp
```
## Run
出力対象 ( -out_components | -out_connections | out_ddl ) を必ず指定します。
```
mywork$ java -jar app-all.jar -out_components -show
```
### Output console ( param: -show)
```
project: EXAMPLE
	job: ComponentRow_0.1
	jobFileName: src/test/resources/testNormal/EXAMPLE/process/t01_Compo/Databases/ComponentRow_0.1.item
		id: tCreateTable_1  type: tCreateTable
		id: tMysqlOutput_1  type: tMysqlOutput
		id: tMysqlRow_1  type: tMysqlRow
		id: tMysqlRow_2  type: tMysqlRow
		id: tRowGenerator_1  type: tRowGenerator
	job: Connection_0.1
```
### Output components excel file ( param: -out_components)
![excel_output](figure/excel_out.png "excel output")

### Output db schema file ( param: -out_connections )
```
connections
	itemFile: mssql_0.1.item, name: mssql
		tableName: allcol_tbl, tableType: TABLE, schemaName: dbo
			columnName: col1, type: INT, length: 10
			columnName: col2, type: BIT, length: 1
			columnName: col3, type: DECIMAL, length: 18
			columnName: col4, type: MONEY, length: 19

```
### Ourput create table sql file ( param: -out_ddl)
```
CREATE TABLE [dbo].[allcol_tbl](
	[col1] [INT] NOT NULL,
	[col2] [BIT] NULL,
	[col3] [DECIMAL](18, 0) NULL,
```

## Test case error for windows
For Windows. Change git config for code of line end, cause of avoid errors in test case.
```
$ git config --global core.autocrlf input
```

## 参考機能
### STATS FILE（統計情報）について
Talend Studioにて各コンポーネントの詳細設定の統計情報出力をチェックすることでコンポーネントごとの実行の開始／終了を統計情報ファイルに出力することが可能です。
ただし、この設定は各コンポーネントごとに手動で行う必要があります。
Studioでの実行時に作成される統計情報ファイルを読み込ませることにより、実行されたかどうか及び実行時間がわかります。
このプログラムでは実行された回数と実行時間の累積をコンポーネントごとに集計します。
