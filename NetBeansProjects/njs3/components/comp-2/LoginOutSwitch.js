/* 
 * LoginOut Switch
 */
import React from 'react';
import Switch from 'rc-switch';

function onChange(value)
{
    console.log(`switch checked: ${value}`); // eslint-disable-line
}

class LoginOutSwitch extends React.Component
{
  state = { disabled: false }

  toggle = () =>
  {
    this.setState({
      disabled: !this.state.disabled
    });
  }

  render() {
    return (
      <div style={{ margin: 20 }}>
        <Switch
          onChange={onChange}
          disabled={this.state.disabled}
          checkedChildren={'开'}
          unCheckedChildren={'关'}
        />
        <div style={{ marginTop: 20 }}>
          <button onClick={this.toggle}>toggle disabled</button>
        </div>
      </div>
    );
  }
}

export {LoginOutSwitch};
