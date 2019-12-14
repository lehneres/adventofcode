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
    def __init__(self, memory, inputs=[]):
        self.isStopped = False
        self.out = []
        self.pointer = 0
        self.init_memory = memory.copy()
        self.memory = memory
        self.inputs = inputs
        self.input_pointer = 0

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
            if len(self.inputs) == 0:
                self.memory[params[0][0]] = int(input("Enter value to continue..."))
            else:
                self.memory[params[0][0]] = self.inputs[self.input_pointer]
                self.input_pointer += 1
            self.pointer += 2

        def output():
            params = get_params(self.memory, self.pointer, 1)
            print('OUT (%s)' % params)
            self.out.append(get_value(self.memory, *params[0]))
            self.pointer += 2
            return get_value(self.memory, *params[0])

        def stop():
            self.isStopped = True

        return {1: addition, 2: multiplication, 3: get_input, 4: output, 5: jump_if_true, 6: jump_if_false, 7: less_than, 8: equals, 99: stop}.get(
                int(str(self.memory[self.pointer])[-2:]))()

    def set_inputs(self, inputs):
        self.inputs = inputs.copy()
        self.input_pointer = 0

    def run(self):
        while not self.isStopped:
            step = self.opcode()
            if step is not None:
                yield step

    def reset(self):
        self.__init__(self.init_memory.copy())
