/* 
 * Table
 */
import React, { Component } from 'react';
import { render } from 'react-dom';
import {Paper,Table,TableHead,TableRow,withStyles}  from '@material-ui/core';

import {CellsExport} from './Comp3b.js';


class ATable extends Component {
  render() {
    return (
        <Paper>
            <Table>
                <TableHead>
                    <TableRow>
                      <CellsExport />
                    </TableRow>
                </TableHead>
            </Table>
        </Paper>
    );
  }
};

export {ATable};