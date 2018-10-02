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


let y = css({
    [`:checked + .${x}`]: { display: 'block' }
});

const handleChange = (event) => { console.log("Switch!"); };

const Switch = () => {
    return(
        <div>
            <input type="checkbox" defaultChecked className={y} />
            <p className={x}>Tada! Here I am!</p>
        </div>
    );
};

render(<Switch />, document.getElementById("t8"));