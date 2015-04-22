@echo off
del \\parwinproto\d$\proto\tioga-jobs-agent\lib\*.jar
copy C:\dvlp\3rd-party\tioga-solutions\Jobs\tioga-jobs-agent\build\install\tioga-jobs-agent\lib\*.jar \\parwinproto\d$\proto\tioga-jobs-agent\lib
copy C:\dvlp\3rd-party\tioga-solutions\Jobs\tioga-jobs-agent\build\install\tioga-jobs-agent\server-start.bat \\parwinproto\d$\proto\tioga-jobs-agent\
