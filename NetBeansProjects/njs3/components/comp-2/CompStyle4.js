/* 
 * Comp Style 4
 */
import React from 'react';
import { render } from 'react-dom';
import glamorous, {ThemeProvider} from 'glamorous';

// Example inspired by http://gridbyexample.com/examples/example12/
const MyGrid = glamorous.div({
  margin: 'auto',
  backgroundColor: '#fff',
  color: '#444',
  border: '1px solid black',
  '@supports (display: grid)': {
    display: 'grid',
    gridGap: 2,
    padding: 2,
    gridTemplateColumns: '200px 125px 125px',
    gridTemplateRows: 'auto',
    gridTemplateAreas: `
      "linput lbutton rbutton"
      "pinput lbutton rbutton"
    `
  }
});

const Box = glamorous.div({
    backgroundColor: 'lightblue',
    color: '#fff',
    borderRadius: 2,
    fontSize: '100%'
});

const Button = glamorous.button(
    {
    width: 125,
    borderRadius: 2,
    color: 'black',
    border: '1px solid black',
    outline: 'none',
    ':hover': {
        backgroundColor: '#bbdefb',
        borderColor: 'darkBlue'
        }
    },
    ({type}) => {
        switch(type) {
            case 'login': return {background:'#8aacc8'}; break;
            case 'register': return {background:'#af8eb5'}; break;
        }
    }
);

const GridApp = () => {
  return (
    <glamorous.Div maxWidth={500}>
        <MyGrid css={{ marginBottom: 1, marginTop: 1 }}>
            <Box css={{ gridArea: 'linput' }}>
                <input type="email" name="email" />
            </Box>
            <Box css={{ gridArea: 'lbutton' }}>
                <Button type="login"
                    onClick={() => {
                        console.log('login');
                    }}>
                    Login
                </Button>
            </Box>
            <Box css={{ gridArea: 'pinput' }}>
                <input type="password" name="password" />
            </Box>
            <Box css={{ gridArea: 'rbutton' }}>
                <Button type="register"
                    onClick={() => {
                        console.log('register');
                    }}>
                    Register
                </Button>
            </Box>
        </MyGrid>
    </glamorous.Div>
  );
};

render(<GridApp />, document.getElementById("s4"));
