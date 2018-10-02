/* 
 * LoginBox
 */
import React, { Component } from 'react';
import { render } from 'react-dom';
import {Paper,TextField,Typography,withStyles} from '@material-ui/core';
import PropTypes from 'prop-types';

import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import indigo from '@material-ui/core/colors/indigo';
import orange from '@material-ui/core/colors/orange';
import pink from '@material-ui/core/colors/pink';
import red from '@material-ui/core/colors/red';

const theme = createMuiTheme({
  palette: {
    primary: {main: '#D84315'},
    secondary: pink,
    error: red,
    // Used by `getContrastText()` to maximize the contrast between the background and
    // the text.
    contrastThreshold: 3,
    // Used to shift a color's luminance by approximately
    // two indexes within its tonal palette.
    // E.g., shift from Red 500 to Red 300 or Red 700.
    tonalOffset: 0.2
  }
});

const styles = theme => ({
  container: {
    display: 'flex',
    flexWrap: 'wrap'
  },
  textField: {
    marginLeft: theme.spacing.unit,
    marginRight: theme.spacing.unit,
    width: 200
  }
});

class LoginBox extends Component {
//    constructor(props)
//    {
//        super(props);
//        this.state = {
//          name: ''
//        };
//    };
    //... Need stage-0 for this type of construct:
    state = {
        name: ''
    };
    
    handleChange = (name) => event => {
        this.setState({
            [name]: event.target.value
        });
    };

    render() {
        const {classes} = this.props;

        return (
            <MuiThemeProvider theme={theme}>
                <Paper>
                    <form className={classes.container} noValidate autoComplete="off">
                        <TextField
                            id="name"
                            label="Name"
                            className={classes.textField}
                            value={this.state.name}
                            onChange={this.handleChange('name')}
                            autoComplete="first-name"
                            margin="normal"
                        />
                        <TextField
                            id="password-input"
                            label="Password"
                            className={classes.textField}
                            type="password"
                            autoComplete="current-password"
                            margin="normal"
                        />
                    </form>
                </Paper>
            </MuiThemeProvider>
        );
    }
};

const LBExport = withStyles(styles)(LoginBox);
export {LBExport};
