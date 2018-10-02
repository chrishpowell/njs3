/* 
 * Test1
 */
import React from 'react';
import { render } from 'react-dom';
import { css } from 'glamor';
import 'typeface-open-sans';

let x = css({
    display: 'none',
    backgroundColor: 'yellow',
    padding: 20
});

// Works
//let y = css({
//    ':hover p': { display: 'block' }
//});

let y = css({
    [`:hover .${x}`]: { display: 'block' }
});

const Switch = () => {
    return(
        <div className={y}>Hover over me
            <p className={x}>Tada! Here I am!</p>
            <p>And here I am</p>
        </div>
    );
};

render(<Switch />, document.getElementById("t6"));