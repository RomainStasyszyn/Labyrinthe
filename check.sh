#!/bin/bash

expected="Grid.java Cellule.java Neighbor.java Laby.java Makefile"
optional="bonus.xml"

function die {
	echo "______________"
	printf "$2" >&2 && echo
	echo "$(tput setaf 1 2>/dev/null)Rejected.$(tput sgr0 2>/dev/null)"
	echo "Please have a look on tp2.pdf"
	exit $1
}
function help {
echo "-----------------------------------"
echo "Welcome to the archive checker"
echo "  This script only test the presence and the absence of expected files."
echo "  Only file listed below as 'Found' will be considered."
echo "  The submission specification is described in 'tp2.pdf', section 4."
echo "-----------------------------------"
}

#test presence of argument (error return: 1)
[ $# -lt 1 ] &&\
	echo "Usage: ./test.sh {archive.tar}" >&2 &&\
	exit 1
[[ "$1" == "--help" ]] || [[ "$1" == "-h" ]] &&\
	help &&\
	exit 0

echo && echo "------- Checking the archive"
ftar="$1"
#test presence of archive (error return: 2)
[ -f "$ftar" ] ||\
	die 2 "$(tput setaf 1 2>/dev/null)ERROR:$(tput sgr0 2>/dev/null) '$ftar' file not found"

#test name of archive (error return: 3)
nameflag=false
echo "$ftar" | egrep "^[-a-z]*_[-a-z]*.tar\$" >/dev/null
if [ $? -eq 0 ]; then
	echo "    Name '$ftar' seems to be $(tput setaf 7 2>/dev/null)well formed$(tput sgr0 2>/dev/null)."
else
	echo "    $(tput setaf 1 2>/dev/null)ERROR:$(tput sgr0 2>/dev/null) the archive name is wrong."
	echo "        expected name: '<firstname>_<lastname>.tar'"
	echo "        where '<lastname>' and '<firstname>' have to be replaced by your own last and first name written in lower case without accent, space and apostrophe."
	nameflag=true
fi

#test validity of the archive (error return: 4)
fin="$(tar tf "$ftar")"
[ $? -ne 0 ] &&\
	die 4 "$(tput setaf 1 2>/dev/null)Error:$(tput sgr0 2>/dev/null) '$ftar' is not a valid archive."

#test presence of expected files (error return: 5)
echo && echo "------- Looking for expected files..."
expflag=false
for i in $expected; do
	echo "$fin" | egrep "^$i\$" >/dev/null
	if [ $? -eq 0 ]; then
		echo "    $(tput setaf 7 2>/dev/null)FOUND:$(tput sgr0 2>/dev/null) '$i'"
		fin="$(echo "$fin" | sed -e "/^${i//\//\\\/}\$/d")"
	else
		expflag=true
		echo "    $(tput setaf 1 2>/dev/null)ERROR:$(tput sgr0 2>/dev/null) '$i' not found!" >&2
	fi
done

#test presence of optional files
echo && echo "------- Looking for optional files..."
optflag=false
for i in $optional; do
	echo "$fin" | egrep "^$i\$" >/dev/null
	if [ $? -eq 0 ]; then
		echo "    $(tput setaf 7 2>/dev/null)FOUND:$(tput sgr0 2>/dev/null) '$i'"
		fin="$(echo "$fin" | sed -e "/^${i//\//\\\/}\$/d")"
	else
		optflag=true
		echo "    $(tput setaf 3 2>/dev/null)WARNING:$(tput sgr0 2>/dev/null) '$i' not found!" >&2
	fi
done

#test presence of additional files
echo && echo "------- Looking for additional files..."
addflag=false
for i in $fin; do
	if echo "$i" | egrep "^.*\.java" >/dev/null; then
		echo "$expected" | egrep "^$i\$" >/dev/null ||\
			echo "$optional" | egrep "^$i\$" >/dev/null
		[ $? -ne 0 ] &&\
			addflag=true &&\
			echo "    $(tput setaf 7 2>/dev/null)FOUND:$(tput sgr0 2>/dev/null) '$i'" &&
			fin="$(echo "$fin" | sed -e "/^${i//\//\\\/}\$/d")"
	fi
done
$addflag && echo "    That's all."
$addflag || echo "    No additional file found."

#echo && echo "------- Looking for $(tput setaf 1 2>/dev/null)non-considered$(tput sgr0) files..."
#if [[ -z "$fin" ]]; then
#	echo "     No other files."
#else
#	echo "$fin" | sed -e "s/^/    $(tput setaf 1 2>/dev/null)NOT CONSIDERED:$(tput sgr0 2>/dev/null) /"
#fi

$expflag && die 5 "$(tput setaf 1 2>/dev/null)ERROR:$(tput sgr0 2>/dev/null) Some required files are missing."
$nameflag && die 3 "$(tput setaf 1 2>/dev/null)ERROR:$(tput sgr0 2>/dev/null) The name of the archive is not valid."
#accept archive and exit (return 0)
echo "______________"
echo "$(tput setaf 7 2>/dev/null)Accepted$(tput sgr0 2>/dev/null)"
exit 0
