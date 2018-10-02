/* 
 * Comp Style 6
 */
import React from 'react';
import { render } from 'react-dom';
import 'normalize.css';  // CSS 'reset'
import '../assets/LIOswitch.less';  // Change this to plain CSS
import glamorous, {ThemeProvider} from 'glamorous';
import { css } from 'glamor';
import Switch from 'rc-switch';
import 'typeface-open-sans';

const AccountGrid = glamorous.section({
    padding: 2,
    backgroundColor: '#fff',
    fontFamily: ["Open Sans","Roboto","Arial"].join(','),
    fontSize: 13,
    color: '#444',
    width: 680,
    height: 55,
    border: '1px solid black',
    '@supports (display: grid)': {
        display: 'grid',
        gridColumnGap: 3,
        gridTemplateColumns: '420px 200px',
        gridTemplateRows: '25px 25px',
        gridTemplateAreas: `
          "row1 inout"
          "row2 search"
        `
    }
});

const AccountDetail = glamorous.section({
    gridArea: 'row1',
    height: '25px',
    lineHeight: '22px',
    borderBottom: '1px solid darkgrey'  // Make gradient...
});

const InOutLocale = glamorous.section({
    gridArea: 'inout',
    marginLeft: 'auto',
    marginRight: 0
});

const InOut = glamorous.section({
    display: 'inline-block',
    marginLeft: 5
});

const Locale = glamorous.section({
    display: 'inline-block'
});

const Buttons = glamorous.section({
    gridArea: 'row2'
});

const Search = glamorous.section({
    gridArea: 'search',
    marginTop: 3,
    height: '100%'
});

const Account = glamorous.section({
    display: 'inline-block',
    height: '90%'
});
const Settings = glamorous.section({
    display: 'inline-block',
    height: '90%'
});
const QandA = glamorous.section({
    display: 'inline-block',
    height: '90%'
});
const Notifications = glamorous.section({
    position: 'relative',
    display: 'inline-block',
    height: '90%'
});
const NotifyCount = glamorous.section({
    position: 'absolute',
    top: 0,
    right: 0,
    background: '#f44336',
    border: '1px solid black',
    paddingLeft: 2,
    paddingBottom: 2,
    width: 14,
    height: 14,
    margin: 0,
    fontSize: 11,
    borderRadius: '50%'
});

const Button = glamorous.button(
    {
    width: 90,
    height: '95%',
    borderRadius: 2,
    color: 'black',
    border: '1px solid black',
    marginTop: 7,
    marginRight: 4,
    outline: 'none',
    ':hover': {
        backgroundColor: '#bbdefb',
        borderColor: 'darkBlue'
        }
    },
    ({type}) => {
        switch(type) {
            case 'account': return {background:'#8aacc8'}; break;
            case 'settings': return {background:'#af8eb5'}; break;
            case 'qna': return {background:'#af8eb5'}; break;
        }
    }
);

const ButtonWithCount = glamorous.button(
    {
    width: 90,
    height: '95%',
    borderRadius: 2,
    color: 'black',
    border: '1px solid black',
    marginTop: 5,
    marginRight: 9,
    outline: 'none',
    ':hover': {
        backgroundColor: '#bbdefb',
        borderColor: 'darkBlue'
        }
    },
    ({type}) => {
        switch(type) {
            case 'notifications': return {background:'#8aacc8'}; break;
        }
    }
);

const Input = glamorous.input({
    width: 250,
    height: 25,
    marginTop: 3,
    boxSizing: 'border-box',
    color: 'black'
});

const Label = glamorous.label({
    position: 'absolute'
});

const onChange = (value) => {
  console.log(`switch checked: ${value}`); // eslint-disable-line
};

var state = { disabled: false };

const AccountGridApp = () => {
  return (
    <AccountGrid>
        <AccountDetail>
            [UserName &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;]: [House &nbsp;&nbsp;] [HSym] [Stone &nbsp;] [Metal ] [Jewel ]
        </AccountDetail>
        <InOutLocale>
            <Locale>
                en-US
            </Locale>
            <InOut>
                <Switch
                    onChange={onChange}
                    disabled={state.disabled}
                    checkedChildren={'Login'}
                    unCheckedChildren={'Logout'}
                />
            </InOut>
        </InOutLocale>
        <Buttons>
            <Account>
                <Button type="account"
                    onClick={() => {
                        console.log('account');
                    }}>
                    Account
                </Button>
            </Account>
            <Settings>
                <Button type="settings"
                    onClick={() => {
                        console.log('settings');
                    }}>
                    Settings
                </Button>
            </Settings>
            <QandA>
                <Button type="qanda"
                    onClick={() => {
                        console.log('qanda');
                    }}>
                    Q & A
                </Button>
            </QandA>
            <Notifications>
                <ButtonWithCount type="notifications"
                    onClick={() => {
                        console.log('notifications');
                    }}>
                    Notifications
                </ButtonWithCount>
                <NotifyCount>9+</NotifyCount>
            </Notifications>
        </Buttons>
        <Search>
            <Input type="text" name="search" id="search" placeholder="Search" />
        </Search>
    </AccountGrid>
  );
};

render(<AccountGridApp />, document.getElementById("s6"));
