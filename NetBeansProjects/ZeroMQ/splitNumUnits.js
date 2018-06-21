/* 
 * Split number into individual units
 */
    function separateNumberIntoUnits(n)
    {
        if (n === 0)
            return [0];

        var arr = [];
        var i = 1;

        while (n > 0)
        {
            arr.unshift((n % 10) * i);
            n = Math.floor(n / 10);
        }

        return arr;
    }

var dayarr = separateNumberIntoUnits(0);
console.log( "0: " +dayarr[0] );
var dayarr = separateNumberIntoUnits(1);
console.log( "1: " +dayarr[0] );
dayarr = separateNumberIntoUnits(9);
console.log( "9: " +dayarr[0] );
dayarr = separateNumberIntoUnits(10);
console.log( "10: " +dayarr[0]+":"+dayarr[1] );
dayarr = separateNumberIntoUnits(11);
console.log( "11: " +dayarr[0]+":"+dayarr[1] );
dayarr = separateNumberIntoUnits(19);
console.log( "19: " +dayarr[0]+":"+dayarr[1] );
dayarr = separateNumberIntoUnits(20);
console.log( "20: " +dayarr[0]+":"+dayarr[1] );
dayarr = separateNumberIntoUnits(29);
console.log( "29: " +dayarr[0]+":"+dayarr[1] );
dayarr = separateNumberIntoUnits(30);
console.log( "30: " +dayarr[0]+":"+dayarr[1] );
dayarr = separateNumberIntoUnits(31);
console.log( "31: " +dayarr[0]+":"+dayarr[1] );