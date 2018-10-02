/* 
 * CompFTest1
 */
import React, { Component } from 'react';
import { render } from 'react-dom';
import {Paper,withStyles} from '@material-ui/core';

function ft1(state) {
    return (
        <React.Fragment>
            <div>{state.name}</div>
        </React.Fragment>
    );
}

function ft2(state) {
    return (
        <React.Fragment>
            <div>{state.password}</div>
        </React.Fragment>
    );
}

class FT1  extends Component {
    state = {
        name: 'Fred Bloggs',
        password: 'password1'
    };
    
    render() {
         return (
            <Paper>
                {ft1(this.state)}
                {ft2(this.state)}
            </Paper>
        );
    }
};

render(<FT1 />,document.getElementById("ftest"));
