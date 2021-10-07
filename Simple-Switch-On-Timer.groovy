//
// Copyright (c) 2020-2021, Denny Page
// Copyright (c) 2021, Alan Eisen
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
// TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
// PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
// NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
// Version 1.0.0    Initial release
// Version 1.1.0    Add App Events
//         1.0.0    Initial Release as "Simple Switch On Timer"
//

definition(
    name: "Simple Switch On Timer",
    namespace: "nsudrivers",
    author: "Alan Eisen (Based on original by Denny Page)",
    description: "Turn a switch on after it has been off for a number of minutes",
    category: "Convenience",
    parent: "nsudrivers:Simple Switch On Timers",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: ""
)

preferences
{
    page(name: "configPage")
}

def configPage()
{
    dynamicPage(name: "", title: "Simple Switch On Timer", install: true, uninstall: true, refreshInterval: 0)
    {
        // Ensure label is correct in case the device has changed label
        checkLabel()

        section("") {
            paragraph "Choose the switch and the number of minutes before the switch is automatically turned On"
        }
        section("") {
            input "configSwitch", "capability.switch", title: "Automatically turn On this switch", multiple: false, required: true
        }
        section("")
        {
            input "configMinutes", "number", title: "Number of minutes before switch is turned on", required: true
        }
        section("")
        {
            input "configSeconds", "number", title: "Number of seconds before switch is turned on", required: true
        }
        section("")
        {
            input name: "appEvents", title: "Enable app events", type: "bool", defaultValue: false
        }
    }
}

def checkLabel()
{
    if (configSwitch)
    {
        oldLabel = app.getLabel()
        newLabel = "${configSwitch} on after ${configMinutes}:${configSeconds} minutes"
        if (newLabel != oldLabel)
        {
            if (oldLabel) log.info "Simple Switch On Timer changed: ${oldLabel} -> ${newLabel}"
            app.updateLabel(newLabel)
        }
    }
}

def installed()
{
    checkLabel()

    if (configMinutes)
    {
        subscribe(configSwitch, "switch", switchEvent)
        if (configSwitch.currentState("switch").value == "off")
        {
            runIn(((configMinutes.toInteger() * 60) + configSeconds.toInteger()), switchOn)
        }
    }
}

def updated() {
    unsubscribe()
    unschedule()
    installed()
}

def switchOn()
{
    String desc = "Switch On Timer: ${configSwitch} turned on after ${configMinutes}:${configSeconds} minutes"
    log.info "${desc}"
    if (appEvents) sendEvent(name: "SSA", value: "On", descriptionText: "${desc}")
    configSwitch.on()
}

def switchEvent(e)
{
    if (e.value == "off")
    {
        String desc = "Switch On Timer: ${configSwitch} On after ${configMinutes}:${configSeconds} minutes scheeduled"
        log.info "${desc}"
        runIn(((configMinutes.toInteger() * 60) + configSeconds.toInteger()), switchOn)
    }
    else if (e.value == "o")
    {
        unschedule()
    }
}
