#! /bin/bash


# your MySQL server's name

SERVER=s2


# directory to backup to

BACKDIR=/root/test/bakmysql


# date format that is appended to filename

DATE=`date +'%m-%d-%Y'`



#----------------------MySQL Settings--------------------#


# your MySQL server's location (IP address is best)

HOST=s2


# MySQL username

USER=root


# MySQL password

PASS=root


# List all of the MySQL databases that you want to backup in here, 

# each separated by a space

DBS=""


# set to 'y' if you want to backup all your databases. this will override

# the database selection above.

DUMPALL=y



#----------------------Mail Settings--------------------#


# set to 'y' if you'd like to be emailed the backup (requires mutt)

MAIL=n


# email addresses to send backups to, separated by a space

EMAILS="fye@xtremeprog.com"


SUBJECT="MySQL backup of database $SERVER ($DATE)"


#----------------------FTP Settings--------------------#


# set "FTP=y" if you want to enable FTP backups

FTP=n


# FTP server settings; should be self-explanatory

FTPHOST="ftp.example.com:21"

FTPUSER="ftpuser"

FTPPASS="password"


# directory to backup to. if it doesn't exist, file will be uploaded to 

# first logged-in directory

FTPDIR="backups"


#-------------------Deletion Settings-------------------#


# delete old files?

DELETE=y


# how many days of backups do you want to keep?

DAYS=10


#----------------------End of Settings------------------#


# check of the backup directory exists

# if not, create it

if  [ -e $BACKDIR ]

then

	echo Backups directory already exists

else

	mkdir -p $BACKDIR

fi


if  [ $DUMPALL = "y" ]

then

	echo "Creating list of all your databases..."


	mysql -h $HOST --user=$USER --password=$PASS -e "show databases;" > dbs_on_$SERVER.txt


	# redefine list of databases to be backed up

	DBS=`sed -e ':a;N;$!ba;s/\n/ /g' -e 's/Database //g' dbs_on_$SERVER.txt`

fi


echo "Backing up MySQL databases..."

for database in $DBS

do

	mysqldump -h $HOST --user=$USER --password=$PASS  --single-transaction  $database > $BACKDIR/$SERVER-mysqlbackup-$database-$DATE.sql



	gzip -f -9 $BACKDIR/$SERVER-mysqlbackup-$database-$DATE.sql

done


# if you have the mail program 'mutt' installed on

# your server, this script will have mutt attach the backup

# and send it to the email addresses in $EMAILS


if  [ $MAIL = "y" ]

then

BODY="db backups"

ATTACH=`for file in $BACKDIR/*$DATE.sql.gz; do echo -n "-a ${file} ";  done`


	echo "$BODY" | mutt -s "$SUBJECT"   $ATTACH  -b  $EMAILS

        

	echo -e "Your backup has been emailed to you! \n"

fi


if  [ $FTP = "y" ]

then

echo "Initiating FTP connection..."

cd $BACKDIR

ATTACH=`for file in *$DATE.sql.gz; do echo -n -e "put ${file}\n"; done`


	ftp -nv <<EOF

	open $FTPHOST

	user $FTPUSER $FTPPASS

	cd $FTPDIR

	$ATTACH

	quit

EOF

echo -e  "FTP transfer complete! \n"

fi


if  [ $DELETE = "y" ]

then
    NEWDATE=`date --date='10 days ago' +'%m-%d-%Y'`

    rm -rf $BACKDIR/$SERVER-mysqlbackup-$database-$NEWDATE.sql.gz

	if  [ $DAYS = "1" ]

	then

		echo "Yesterday's backup has been deleted."

	else

		echo "The backup from $DAYS days ago has been deleted."

	fi

fi


echo Your backup is complete!
