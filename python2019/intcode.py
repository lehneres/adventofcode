def get_params(nmemory, pointer, n):
    return list(zip(nmemory[pointer + 1: pointer + 1 + n], reversed(list(map(int, list(str(nmemory[pointer])[:-2].zfill(n)))))))


class IntCodeMachine:
    MEMORY_LENGTH = 1024 * 1024

    def __init__(self, memory, inputs=[]):
        memory.extend([0] * (self.MEMORY_LENGTH - len(memory)))
        self.isStopped = False
        self.output = []
        self.pointer = 0
        self.init_memory = memory.copy()
        self.memory = memory
        self.inputs = inputs
        self.input_pointer = 0
        self.relative_base = 0

    def get_value(self, val, mode):
        if mode == 0:
            return self.memory[val]
        elif mode == 1:
            return val
        elif mode == 2:
            return self.memory[self.relative_base + val]
        else:
            raise Exception('unknown parameter mode')

    def set_value(self, pointer, mode, val):
        if mode == 0:
            self.memory[pointer] = val
        elif mode == 2:
            self.memory[self.relative_base + pointer] = val
        else:
            raise Exception('unknown parameter mode')

    def opcode(self):
        def addition():
            params = get_params(self.memory, self.pointer, 3)
            print('ADD (%s) : %d + %d' % (params, self.get_value(*params[0]), self.get_value(*params[1])))
            self.set_value(*params[2], self.get_value(*params[0]) + self.get_value(*params[1]))
            self.pointer += 4

        def multiplication():
            params = get_params(self.memory, self.pointer, 3)
            print('MUL (%s) : %d * %d' % (params, self.get_value(*params[0]), self.get_value(*params[1])))
            self.set_value(*params[2], self.get_value(*params[0]) * self.get_value(*params[1]))
            self.pointer += 4

        def jump_if_true():
            params = get_params(self.memory, self.pointer, 2)
            print('JIT (%s) : %d => %d' % (params, self.get_value(*params[0]), self.get_value(*params[1])))
            if self.get_value(*params[0]) != 0:
                self.pointer = self.get_value(*params[1])
            else:
                self.pointer += 3

        def jump_if_false():
            params = get_params(self.memory, self.pointer, 2)
            print('JIF (%s) : %d => %d' % (params, self.get_value(*params[0]), self.get_value(*params[1])))
            if self.get_value(*params[0]) == 0:
                self.pointer = self.get_value(*params[1])
            else:
                self.pointer += 3

        def less_than():
            params = get_params(self.memory, self.pointer, 3)
            print('LES (%s) : %d ? %d' % (params, self.get_value(*params[0]), self.get_value(*params[1])))
            self.set_value(*params[2], int(self.get_value(*params[0]) < self.get_value(*params[1])))
            self.pointer += 4

        def equals():
            params = get_params(self.memory, self.pointer, 3)
            print('EQU (%s) : %d ? %d' % (params, self.get_value(*params[0]), self.get_value(*params[1])))
            self.set_value(*params[2], int(self.get_value(*params[0]) == self.get_value(*params[1])))
            self.pointer += 4

        def get_input():
            params = get_params(self.memory, self.pointer, 1)
            print('IN (%s)' % params)
            if len(self.inputs) == 0:
                self.set_value(*params[0], int(input("Enter value to continue...")))
            else:
                self.set_value(*params[0], self.inputs[self.input_pointer])
                self.input_pointer += 1
            self.pointer += 2

        def output():
            params = get_params(self.memory, self.pointer, 1)
            print('OUT (%s)' % params)
            self.output.append(self.get_value(*params[0]))
            self.pointer += 2
            return self.get_value(*params[0])

        def adjust_base():
            params = get_params(self.memory, self.pointer, 1)
            print('ADJ (%s)' % params)
            self.relative_base += self.get_value(*params[0])
            self.pointer += 2

        def stop():
            self.isStopped = True

        return {1: addition, 2: multiplication, 3: get_input, 4: output, 5: jump_if_true, 6: jump_if_false, 7: less_than, 8: equals, 9: adjust_base, 99: stop}.get(
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
