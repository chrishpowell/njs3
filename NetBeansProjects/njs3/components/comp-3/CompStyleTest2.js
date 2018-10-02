/* 
 * test2
 */
import React from 'react';
import { render } from 'react-dom';
import { css } from 'glamor';

let bh = css({ 
  ':hover > p': {
    backgroundColor: 'blue' 
  } 
});

const Switch = () => {
    return(
        <div className={bh}>
            Hover over me
            <p>Should be blue</p>
        </div>
    );
};

render(<Switch />, document.getElementById("t7"));
