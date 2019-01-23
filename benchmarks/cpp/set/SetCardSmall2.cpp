#include <iostream>
#include <string>
#include "BUtils.cpp"
#include "BSet.cpp"
#include "BInteger.cpp"
#include "BBoolean.cpp"

#ifndef SetCardSmall2_H
#define SetCardSmall2_H

using namespace std;

class SetCardSmall2 {



    private:



        BInteger counter;
        BSet<BInteger > set;
        BInteger result;

    public:

        SetCardSmall2() {
            counter = (BInteger(0));
            set = (BSet<BInteger >((BInteger(1))));
            result = (BInteger(0));
        }

        void simulate() {
            while((counter.less((BInteger(5000000)))).booleanValue()) {
                result = set.card();
                counter = counter.plus((BInteger(1)));
            }
        }

};
int main() {
    clock_t start,finish;
    double time;
    SetCardSmall2 exec;
    start = clock();
    exec.simulate();
    finish = clock();
    time = (double(finish)-double(start))/CLOCKS_PER_SEC;
    printf("%f\n", time);
    return 0;
}
#endif

