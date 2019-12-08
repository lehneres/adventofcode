def get_value(nmemory, val, mode):
    if mode == 0:
        return nmemory[val]
    elif mode == 1:
        return val
    else:
        raise Exception('unknown parameter mode')


def get_params(nmemory, pointer, n):
    return list(zip(nmemory[pointer + 1: pointer + 1 + n], reversed(list(map(int, list(str(nmemory[pointer])[:-2].zfill(n)))))))


class IntCodeMachine:
    def __init__(self, memory):
        self.isStopped = False
        self.out = []
        self.pointer = 0
        self.init_memory = memory.copy()
        self.memory = memory

    def opcode(self):
        def addition():
            params = get_params(self.memory, self.pointer, 3)
            print('ADD (%s) : %d + %d' % (params, get_value(self.memory, *params[0]), get_value(self.memory, *params[1])))
            self.memory[params[2][0]] = get_value(self.memory, *params[0]) + get_value(self.memory, *params[1])
            self.pointer += 4

        def multiplication():
            params = get_params(self.memory, self.pointer, 3)
            print('MUL (%s) : %d * %d' % (params, get_value(self.memory, *params[0]), get_value(self.memory, *params[1])))
            self.memory[params[2][0]] = get_value(self.memory, *params[0]) * get_value(self.memory, *params[1])
            self.pointer += 4

        def jump_if_true():
            params = get_params(self.memory, self.pointer, 2)
            print('JIT (%s) : %d => %d' % (params, get_value(self.memory, *params[0]), get_value(self.memory, *params[1])))
            if get_value(self.memory, *params[0]) != 0:
                self.pointer = get_value(self.memory, *params[1])
            else:
                self.pointer += 3

        def jump_if_false():
            params = get_params(self.memory, self.pointer, 2)
            print('JIF (%s) : %d => %d' % (params, get_value(self.memory, *params[0]), get_value(self.memory, *params[1])))
            if get_value(self.memory, *params[0]) == 0:
                self.pointer = get_value(self.memory, *params[1])
            else:
                self.pointer += 3

        def less_than():
            params = get_params(self.memory, self.pointer, 3)
            print('LES (%s) : %d ? %d' % (params, get_value(self.memory, *params[0]), get_value(self.memory, *params[1])))
            self.memory[params[2][0]] = int(get_value(self.memory, *params[0]) < get_value(self.memory, *params[1]))
            self.pointer += 4

        def equals():
            params = get_params(self.memory, self.pointer, 3)
            print('EQU (%s) : %d ? %d' % (params, get_value(self.memory, *params[0]), get_value(self.memory, *params[1])))
            self.memory[params[2][0]] = int(get_value(self.memory, *params[0]) == get_value(self.memory, *params[1]))
            self.pointer += 4

        def get_input():
            params = get_params(self.memory, self.pointer, 1)
            print('IN (%s)' % params)
            self.memory[params[0][0]] = int(input("Enter value to continue..."))
            self.pointer += 2

        def print_stdout():
            params = get_params(self.memory, self.pointer, 1)
            print('OUT (%s)' % params)
            self.out.append(get_value(self.memory, *params[0]))
            self.pointer += 2

        def stop():
            self.isStopped = True

        return {1: addition, 2: multiplication, 3: get_input, 4: print_stdout, 5: jump_if_true, 6: jump_if_false, 7: less_than, 8: equals, 99: stop}.get(
                int(str(self.memory[self.pointer])[-2:]))()

    def run(self):
        while not self.isStopped:
            self.opcode()

    def reset(self):
        self.__init__(self.init_memory.copy())
