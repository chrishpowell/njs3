/* 
 * Cells
 */
import React, { Component } from 'react';
import { render } from 'react-dom';
import {TableCell,Typography,withStyles} from '@material-ui/core';

const styles = {
    root: {
        margin: 10,
        padding: 5,
        borderColor: 'red',
        borderWidth: 1,
        borderStyle: 'solid'
    }
};

class Cells extends Component {
  render() {
    const {classes} = this.props;
    
    return (
      <React.Fragment>
        <TableCell className={classes.root}>Hello</TableCell>
        <TableCell>World</TableCell>
      </React.Fragment>
    );
  }
};

const CellsExport = withStyles(styles)(Cells);
export {CellsExport};