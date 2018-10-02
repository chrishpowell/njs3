/* 
 * Comp Style 5
 */
import React from 'react';
import { render } from 'react-dom';
import glamorous, {ThemeProvider} from 'glamorous';
import 'typeface-open-sans';

// Example inspired by http://gridbyexample.com/examples/example12/
const MyGrid = glamorous.section({
    padding: 2,
    backgroundColor: '#fff',
    fontFamily: ["Open Sans","Roboto","Arial"].join(','),
    fontSize: 14,
    color: '#444',
    width: 398,
    border: '1px solid black',
    '@supports (display: grid)': {
        display: 'grid',
        gridGap: 3,
        gridTemplateColumns: '250px 70px 70px',
        gridTemplateRows: 'auto',
        gridTemplateAreas: `
          "linput lbutton rbutton"
          "pinput lbutton rbutton"
        `
    }
});

const Email = glamorous.section({
    gridArea: 'linput'
});

const Login = glamorous.section({
    gridArea: 'lbutton',
    paddingLeft: 4
});

const Password = glamorous.section({
    gridArea: 'pinput'
});

const Register = glamorous.section({
    gridArea: 'rbutton'
});

const Button = glamorous.button(
    {
    width: '100%',
    height: '100%',
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

const Input = glamorous.input({
    width: 250,
    color: 'black'
});

const Label = glamorous.label({
    position: 'absolute'
});

const GridApp = () => {
  return (
    <MyGrid>
        <Email>
            <Input type="email" name="email" id="email" placeholder="email" />
        </Email>
        <Login>
            <Button type="login"
                onClick={() => {
                    console.log('login');
                }}>
                Login
            </Button>
        </Login>
        <Password>
            <Input type="password" name="password" id="password" placeholder="password" />
        </Password>
        <Register>
            <Button type="register"
                onClick={() => {
                    console.log('register');
                }}>
                Register
            </Button>
        </Register>
    </MyGrid>
  );
};

render(<GridApp />, document.getElementById("s5"));
