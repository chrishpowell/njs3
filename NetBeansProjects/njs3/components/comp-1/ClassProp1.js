/* 
 */
import React, { Component } from 'react';
import { render } from 'react-dom';
import { Paper,
         withStyles } from '@material-ui/core';
import PropTypes from 'prop-types';

const styles = theme => ({
  container: {
      border: '1px solid red'
  }
});

class CP1 extends Component
{   
    render() {
        const { classes } = this.props;

        return(
            <div className={classes.container}>CP1</div>
        );
    }
};

render(<CP1 />,document.getElementById("cp1"));
