.SUFFIXES:

.PHONY: all clean

all: Counter10000 CAN_BUS_tlc_exec LiftExec scheduler_deterministic_exec Sieve Train_1_beebook_deterministic_exec

PROBCLI=./probcli.sh
OUTPUT ?= runtimes.txt

% :
	gtime -f "%C %E %M" -ao $(OUTPUT) $(PROBCLI) $@.mch -init -execute 1 -noass -noinv -nogoal -p COMPRESSION TRUE

clean:
	rm *.prob
