;(function (angular) {
    "use strict";

    angular.module("core.ui")
        .component("snmCombobox", {
            templateUrl: function (ResourcesService) {
                return ResourcesService.getTemplate('ui/combobox');
            },
            bindings: {
                form: "=",
                name: "@",
                input: "=",
                mask: "@",
                inputPlaceholder: "=",
                list: "=",
                listName: "@"
            },
            controller(ApiService, Notification) {
                let $ctrl = this;
                Object.assign($ctrl, {
                    $onInit() {
                        $ctrl.inputValue = ($ctrl.input) ? $ctrl.getValue($ctrl.input) : '';
                        if ($ctrl.form) {
                            if ($ctrl.name) $ctrl.requiredInput = true;
                            if ($ctrl.listName) $ctrl.requiredList = true;
                        }
                    },
                    requiredInput: false,
                    requiredList: false,
                    inputListValue: null,
                    isOpenDropDown: false,
                    outsideClick() {

                    },
                    activeClass() {
                        return ($ctrl.isOpenDropDown) ? '_active' : '';
                    },
                    getValue(item) {
                        if ($ctrl.mask) return $ctrl.parseMask(item);
                        return item;
                    },
                    parseMaskItem(item) {
                        if (item) {
                            let str = $ctrl.mask;
                            const keys = str.match(/[^{}]+(?=})/g);
                            const values = str.match(/\{([^\}]*)\}/g);

                            values.forEach(function (value, ndx) {
                                const key = keys[ndx];
                                let valueItem = null;

                                if (key.split('|').length > 1) {
                                    for (let i = 0; i < key.split('|').length; i++) {
                                        const value = key.split('|')[i];
                                        if (item[value]) {
                                            valueItem = item[value];
                                            break;
                                        }
                                    }
                                } else {
                                    valueItem = item[key]
                                }
                                str = str.replace(value, (valueItem) ? valueItem : '');
                            });

                            return  str;
                        }
                        return item;
                    },
                    parseMask(items) {
                        if (typeof items === 'object' && items.length === undefined) {
                            return $ctrl.parseMaskItem(items);
                        }

                        return items.map((item) => $ctrl.parseMaskItem(item));
                    },
                    checkListValues(item) {
                        return $ctrl.list.some((itemList) => itemList.id === item.id)
                    },
                    pushToList(item) {
                        const result = $ctrl.list.some((listItem) => {
                            return listItem.id === item.id;
                        });

                        if (result) {
                            Notification.error({message: 'Данный элемент уже присутствует в списке', delay: 2500});
                            return
                        }

                        $ctrl.list.push(item)
                    },
                    removeItemFromList(item) {
                        $ctrl.list.map((itemList, ndx) => {
                            if (itemList.id === item.id) {
                                $ctrl.list.splice(ndx, 1)
                            }
                        });

                        if ($ctrl.form) {
                            ($ctrl.listName && $ctrl.list.length === 0) ? (
                                $ctrl.form[$ctrl.listName].$setValidity("required", false)
                            ) : (
                                $ctrl.form[$ctrl.listName].$setValidity("required", true)
                            );
                            $ctrl.form.$setDirty();
                        }

                    },
                    onSelectItem(item) {
                        $ctrl.inputValue = $ctrl.getValue(item);
                        $ctrl.dropdownItems = [];
                        $ctrl.isOpenDropDown = false;
                        if ($ctrl.form) {
                            if ($ctrl.name) $ctrl.form[$ctrl.name].$setValidity("required", true);
                            if ($ctrl.listName) $ctrl.form[$ctrl.listName].$setValidity("required", true);
                            $ctrl.form.$setDirty();
                        }
                        if ($ctrl.list) {
                            $ctrl.pushToList(item);
                            $ctrl.inputValue = '';
                        }
                        if ($ctrl.input === undefined) return;
                        $ctrl.input = item;
                    },
                    handleTrigger() {
                        if ($ctrl.isOpenDropDown) {
                            $ctrl.dropdownItems = [];
                            $ctrl.isOpenDropDown = false;
                            $ctrl.inputValue = ''
                        } else {
                            $ctrl.getParticipants();
                        }
                    },
                    getParticipants() {
                        ApiService.participants.getProducers().then(({data}) => {
                            $ctrl.dropdownItems = [];

                            data.results.forEach(function (item) {
                                if (item.name) $ctrl.dropdownItems.push(item);
                            });

                            $ctrl.isOpenDropDown = true;
                        });
                    },
                    onChangeInput() {
                        ApiService.participants.searchByName($ctrl.inputValue).then(({data}) => {
                            $ctrl.dropdownItems = [];

                            data.results.forEach(function (item) {
                                $ctrl.dropdownItems.push(item);
                            });

                            $ctrl.isOpenDropDown = true;
                        }).catch(err => {
                            $ctrl.dropdownItems = [];
                            Notification.error({message: err.statusText, delay: 2500});
                            $ctrl.isOpenDropDown = true;
                        });

                        if ($ctrl.form) {
                            if ($ctrl.name) $ctrl.form[$ctrl.name].$setValidity("required", false);
                        }
                    }
                })
            }
        })
})(angular);