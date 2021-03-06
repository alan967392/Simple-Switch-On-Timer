//
// Copyright (c) 2020, Denny Page
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
// Simple Switch Off Timer:
// Version 1.0.0    Initial release
//
// Simple Switch On Timer:
//         1.0.0    Initial Release as "Simple Switch On Timer"


definition(
    name: "Simple Switch On Timers",
    namespace: "nsudrivers",
    author: "Alan Eisen (orignal by Denny Page)",
    description: "Turn switches on after they have been on for a number of minutes",
    category: "Convenience",
    singleInstance: true,
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
    dynamicPage(name: "", title: "Simple Switch On Timers", install: true, uninstall: true, refreshInterval: 0)
    {
        if (app.getInstallationState() == 'COMPLETE')
        {
            // Ensure child labels are correct in case a device has changed label
            childApps.each
            {
                child -> child.checkLabel()
            }

            section("")
            {
                paragraph "Automtatically turn switches on after they have been on a number of minutes."
            }
            section
            {
                app(name: "childApps", appName: "Simple Switch On Timer", namespace: "nsudrivers", title: "Create A New Simple Switch On Timer", multiple: true)
            }
        }
        else
        {
            section("")
            {
                paragraph "<b>Click Done to complete the installation.</b>"
            }
        }
    }

}

def installed()
{
    log.info "There are ${childApps.size()} Simple Switch On Timers"
    childApps.each
    {
        child -> log.info "  Simple Switch On Timer: ${child.label}"
    }
}

def updated()
{
    unsubscribe()
    installed()
}
