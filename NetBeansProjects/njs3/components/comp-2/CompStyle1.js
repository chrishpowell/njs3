/* 
 * Comp Style 1
 */
import React from 'react';
import { render } from 'react-dom';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import * as colors from '@material-ui/core/colors';
import Button from '@material-ui/core/Button';
import 'typeface-open-sans';

const theme = createMuiTheme({
//    palette: {
//        primary: { main: '#607D8B', light: '#607D8B', dark: '89abc6', contrastText: '#1B5E20'  },
//        secondary: { main: '#fff' }
//    },
    typography: {
        fontFamily: ["Open Sans","Roboto","Arial"].join(',')
//        button: {
//            textTransform: 'none',
//            fontFamily: ["Open Sans","Roboto","Arial"].join(',')
    },
    overrides: {
        MuiButton: {
            root: {
                background: 'lightBlue',
                borderRadius: 2,
                border: 1,
                color: 'red',
                height: 36,
                padding: '0 30px',
                boxShadow: '0 3px 5px 2px rgba(255, 105, 135, .3)'
            },
            outlined: {
                background: 'red',
                borderRadius: 2,
                border: 1,
                color: 'green',
                height: 36,
                padding: '0 30px',
                boxShadow: '0 3px 5px 2px rgba(255, 105, 135, .3)'
            }
        }
    }
});

function Palette() {
  return (
    <MuiThemeProvider theme={theme}>
      <Button variant="outlined">Primary</Button>
      <Button>Secondary</Button>
    </MuiThemeProvider>
  );
}

/*export default Palette;*/
render(<Palette />,document.getElementById("s1"));
