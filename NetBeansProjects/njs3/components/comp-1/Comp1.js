/* 
 * Comp1
 */
import _ from 'lodash';
import React, { Component } from 'react';
import { render } from 'react-dom';
import './css/style1.css';
import Icon from './images/discoveri.png';
import Button from '@material-ui/core/Button';

function component()
{
    let element = document.createElement('div');
    element.innerHTML = _.join(['Hello', 'Crispy!'], ' ');
    element.classList.add('hello');
    
    var discvIcon = new Image();
    discvIcon.src = Icon;
    element.appendChild(discvIcon);

    return element;
}

const App = () => (
    <Button variant="contained" color="primary">
      Hello World
    </Button>
);

var n = document.getElementById("njs3");
n.appendChild(component());
render(<App />,React.cloneElement(n));
