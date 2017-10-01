#!/usr/bin/env python
import os
import subprocess
import pdb
import re
import sys

def executeShell(command):
    proc = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE)
    output = ""
    while True:
        line = proc.stdout.readline()
        output += line + "\n"
        if line != '':
            print line.rstrip()
        else:
            break
    return output

executeShell("git checkout master")

POSTFIX = ""

oldVersionNumber = executeShell("perl -ne 'if(m/libraryVersionName\s*=\s*\"(.*?)\"/){ print $1; }' ./build.gradle")
tokens = oldVersionNumber.split(".")
prefix = ".".join(tokens[:-1])
lastVersion = tokens[-1]
lastVersionInt = int(re.sub("\D", "", lastVersion))
lastVersionInt += 1
newVersion = prefix + "." + str(lastVersionInt) + POSTFIX
newVersionRegex = newVersion.replace(".", "\.")

executeShell("sed -i '' 's/\(coreVersionName *= *\"\)\(.*\)\(\"\)/\\1" + newVersionRegex + "\\3/' ./build.gradle")

print "Updated core version with " + newVersion
executeShell("git add ./build.gradle")
executeShell("git commit -m \" Updated core version to " + newVersion + "\"")
tag = "v" + newVersion
executeShell("git tag " + tag)
executeShell("git push origin master")
executeShell("git push origin " + tag)
executeShell("./gradlew clean assemble bintrayUpload -PdryRun=false")

