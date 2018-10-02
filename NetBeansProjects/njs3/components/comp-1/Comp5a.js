/* 
 * LoginBox2
 */
import React, { Component } from 'react';
import { render } from 'react-dom';
import { Paper,
         Button,
         Grid,
         InputLabel,
         FormControl,
         Input,
         withStyles } from '@material-ui/core';
import PropTypes from 'prop-types';

import { MuiThemeProvider,
         createMuiTheme } from '@material-ui/core/styles';
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
  formControl: {
    margin: theme.spacing.unit
  },
  textField: {
    marginLeft: theme.spacing.unit,
    marginRight: theme.spacing.unit,
    width: 200
  }
});

// The `withStyles()` higher-order component is injecting a `classes`
// property that is used by the `Button` component.
const AstroButton = withStyles({
  root: {
    background: 'linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)',
    borderRadius: 3,
    border: 0,
    color: 'white',
    height: 48,
    fullWidth: true,
    padding: '0 30px',
    boxShadow: '0 3px 5px 2px rgba(255, 105, 135, .3)'
  },
  label: {
    textTransform: 'capitalize'
  }
})(Button);

const AstroGrid = withStyles({
    item: {
        border: '1px solid red'
    }
})(Grid);

class LoginBox extends Component
{
    state = {
        name: '',
        password: ''
    };

    handleChange = (name) => event => {
        this.setState({
            [name]: event.target.value
        });
    };

    render() {
        const { classes } = this.props;
        
        return (
            <MuiThemeProvider theme={theme}>
                <Paper>
                    <Grid container direction="row" justify="flex-start" spacing={24}>
                        <Grid item xs={6}>
                          <FormControl className={classes.formControl}>
                            <InputLabel htmlFor="name-simple">Name</InputLabel>
                            <Input id="name-simple" value={this.state.name} onChange={this.handleChange('name')} />
                          </FormControl>
                        </Grid>
                        <Grid item xs={6}>
                          <AstroButton variant="contained">Login</AstroButton>
                        </Grid>
                        <Grid item xs={6}>
                          <FormControl className={classes.formControl}>
                            <InputLabel htmlFor="password">Password</InputLabel>
                            <Input id="pwd" type="password" value={this.state.password} onChange={this.handleChange('password')} />
                          </FormControl>
                        </Grid>
                        <AstroGrid item xs={6}>
                          <AstroButton variant="contained">Register</AstroButton>
                        </AstroGrid>
                    </Grid>
                </Paper>
            </MuiThemeProvider>
        );
    }
};

const LBExport2 = withStyles(styles)(LoginBox);
export {LBExport2};


