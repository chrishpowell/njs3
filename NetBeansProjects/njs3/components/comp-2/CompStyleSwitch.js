/* 
 * Switch
 */
import React from 'react';
import glamorous, {ThemeProvider} from 'glamorous';
import { css } from 'glamor';
import 'typeface-open-sans';

let divvy = css({
    height: 34,
    width: 48,
    position: 'relative',
    display: 'inline-block',
    '& input': { opacity: 0, display: 'none' }
});

let slider = css({
    position: 'absolute',
    cursor: 'pointer',
    height: 20,
    width: 50,
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'lightGrey',
    WebkitTransition: '.4s',
    transition: '.4s',
    borderRadius: 27,
    ':before': {
        position: 'absolute',
        content: "!!",
        height: 20,
        width: 20,
        left: 0,
        bottom: 0,
        backgroundColor: 'lightBlue',
        WebkitTransition: '.4s',
        transition: '.4s'
    }
});

let iclass = css({
    height: 20,
    width: 200
//    [`:checked + .${slider}`]: { backgroundColor: 'lightGreen' }
// [`:hover .${x}`]:
//        ' slider:before': {
//                WebkitTransform: 'translateX(26px)',
//                msTransform: 'translateX(26px)',
//                transform: 'translateX(26px)'
//                }
//    },
//    [`:focus + .${slider}`]: { boxShadow: '0 0 1px #2196F3' }  // Duplicate of {slider} error??
});

const handleChange = (event) => { console.log("Switch!"); document.getElementById("slider").style.backgroundColor='red'; };

const Switch = () => {
    return(
        <label className={divvy}>
            <input type="checkbox" checked onChange={handleChange} className={iclass} />
            <span id="slider" className={slider}></span>
        </label>
    );
};

export {Switch};
