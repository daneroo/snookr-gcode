import sys
import os
import string
import time

######################################################
# logging stubs section
######################################################

def logError(msg):
	sys.stderr.write(msg)
	sys.stderr.write("\n")
def logWarn(msg):
	logError(msg)
def logInfo(msg):
	logError(msg)

######################################################
# sqlite noarch section
######################################################
def getScalar(conn,sql):
        curs = conn.cursor()
        curs.execute(sql)
        row = curs.fetchone()
        curs.close()
        return row[0]
	
def SqliteConnectNoArch(filename): # return either a sqlite3/2/1 connection
	if not os.path.exists(filename):
		logError("Could not find SQLite3 db file: %s" % filename)
		sys.exit(0);
	try:
		from pysqlite2 import dbapi2 as sqlite
		logInfo('Using sqlite-2')
		return sqlite.connect(filename)
	except:
		pass
#		logWarn("from pysqlite2 import dbapi2 failed")
	try:
		import sqlite3 as sqlite;
		logInfo('Using sqlite-3')
		return sqlite.connect(filename)
	except:
		pass
#		logWarn("import sqlite3 failed")
	try:
		import sqlite
		logInfo('Using sqlite-1')
		return sqlite.connect(filename)
	except:
		pass
#		logWarn("import sqlite failed")
	return None
	
######################################################
# TED time section
######################################################
def cnvTime(tedTimeString):
	secs = ( string.atol(tedTimeString) / 10000 - 62135582400000 ) / 1000
	return time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(secs))

def invTime(secs):
        tedTimeLong = long( ((secs * 1000) +  62135582400000)*10000 ) 
        return "%019ld" % tedTimeLong

def testTime():
        secs = time.time()
        log("Testing time from secs:%d" % secs)
        stamp = time.strftime("%Y-%m-%d %H:%M:%S",time.localtime(secs))
        ted = invTime(secs)
        log("stamp=%s invTime=%s" % ( stamp, ted ) )
        log("ted=  %s cnvTime=%s" % ( ted, cnvTime(ted) ) )
        sys.exit(0)




if __name__ == "__main__":
	
	import sys
	import os
	if len(sys.argv) < 2:
		print "usage : %s <SQLite3 db file>" % sys.argv[0]
		sys.exit(0);
	else:
		filename = sys.argv[1]

	conn = SqliteConnectNoArch(filename)
	c = conn.cursor()
	c.execute('select * from rdu_second_data limit 10')
	for row in c:
		print "| %s | %f | %f | %f |" % row