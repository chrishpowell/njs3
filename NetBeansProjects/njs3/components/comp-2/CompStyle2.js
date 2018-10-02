/* 
 * Comp Style 2
 */
import React from 'react';
import { render } from 'react-dom';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import 'typeface-open-sans';

const theme = createMuiTheme({
    typography: {
        fontSize: 12,
        fontFamily: ["Open Sans","Roboto","Arial"].join(',')
    },
    overrides: {
        MuiButton: {
            root: {
                background: 'lightgrey'
            },
            raised: {
                background: '#8aacc8',
                color: 'black',
                border: '1px solid black',
                '&:hover': {
                    backgroundColor: '#bbdefb',
                    borderColor: 'darkBlue'
                }
            },
            outlined: {
                background: '#af8eb5',
                color: 'black',
                border: '1px solid black',
                '&:hover': {
                    backgroundColor: '#e1bee7',
                    borderColor: 'darkBlue'
                }
            }
        }
    }
});

function Palette() {
  return (
    <MuiThemeProvider theme={theme}>
      <Button variant="raised" size="small">Raised</Button>
      <Button variant="outlined" size="small">Outlined</Button>
      <Button>Text</Button>
    </MuiThemeProvider>
  );
}

/*export default Palette;*/
render(<Palette />,document.getElementById("s2"));
