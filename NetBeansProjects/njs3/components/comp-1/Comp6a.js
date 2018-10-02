/* 
 */
import React, { Component } from 'react';
import { render } from 'react-dom';
import PropTypes from 'prop-types';
import { Button,
         Grid,
         withStyles } from '@material-ui/core';

/*const AButton = withStyles({
  root: {
    background: 'lightblue',
    borderRadius: 3,
    border: 0,
    color: 'white',
    height: 30,
    fullWidth: true
  },
  label: {
    textTransform: 'capitalize'
  }
})(Button);

const AGrid = withStyles({
    item: {
        border: '1px solid red'
    }
})(Grid);*/

const styles = theme => ({
  container: {
    display: 'grid',
    gridTemplateColumns: 'repeat(12, 1fr)',
    gridGap: `${theme.spacing.unit * 3}px`
  }
});

const lbox = { gridColumn: '1 / 2', gridRow: '1 / 2' };

class Test6 extends Component
{   
    render() {
        const { classes } = this.props;

        return(
            /*<AGrid container>
                <AGrid item xs={6}>
                  <AButton>A Button</AButton>
                </AGrid>
            </AGrid>*/
            <div className={classes.container}>
                <div style={{...lbox}}>
                  <Paper className={classes.paper}>xs=3</Paper>
                </div>
            </div>
        );
    }
};

render(<Test6 />,document.getElementById("test6"));
